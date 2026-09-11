import { useRef, useState } from 'react';

/**
 * OtpInput — Production-ready OTP (One-Time Password) component
 *
 * WHY useRef here:
 * ─────────────────────────────────────────────────────────────────
 * We need to imperatively FOCUS individual input boxes in response
 * to user actions (typing, backspace, paste). This is DOM
 * manipulation that can't be done with state alone — you need a
 * direct handle to the actual DOM element. That's exactly what
 * useRef provides.
 *
 * useRef usages in this component:
 *  1. inputRefs  — array of refs, one per digit box → lets us call
 *                  .focus() on any box programmatically.
 *  2. hasSubmitted — a flag that persists across renders WITHOUT
 *                  causing a re-render (unlike useState). Used to
 *                  prevent double-submissions.
 *
 * Key difference: useState re-renders on change, useRef does NOT.
 */

const OTP_LENGTH = 6;

export default function OtpInput({ onVerify }) {
  const [digits, setDigits] = useState(Array(OTP_LENGTH).fill(''));
  const [status, setStatus]   = useState('idle'); // idle | loading | success | error
  const [message, setMessage] = useState('');

  // ── useRef usage 1: DOM refs ────────────────────────────────────────────────
  // inputRefs.current is an array — inputRefs.current[i] points to the i-th <input> DOM node.
  // Calling inputRefs.current[i].focus() moves the cursor there — no re-render triggered.
  const inputRefs = useRef(Array(OTP_LENGTH).fill(null));

  // ── useRef usage 2: mutable value that survives re-renders ─────────────────
  // hasSubmitted.current changes don't trigger re-renders — perfect for flags/timers/IDs.
  const hasSubmitted = useRef(false);

  // ── Handlers ──────────────────────────────────────────────────────────────

  const focusBox = (index) => {
    // Guard against out-of-bounds
    if (index >= 0 && index < OTP_LENGTH) {
      inputRefs.current[index]?.focus();
    }
  };

  const handleChange = (e, index) => {
    const val = e.target.value;

    // Accept only digits
    if (!/^\d*$/.test(val)) return;

    // Take only the last character (in case browser pastes on input)
    const digit = val.slice(-1);

    const updated = [...digits];
    updated[index] = digit;
    setDigits(updated);

    // Auto-advance to next box when a digit is entered
    if (digit && index < OTP_LENGTH - 1) {
      focusBox(index + 1);
    }
  };

  const handleKeyDown = (e, index) => {
    if (e.key === 'Backspace') {
      if (digits[index]) {
        // Clear current box
        const updated = [...digits];
        updated[index] = '';
        setDigits(updated);
      } else {
        // Box already empty — move back and clear previous
        const updated = [...digits];
        updated[index - 1] = '';
        setDigits(updated);
        focusBox(index - 1);
      }
    } else if (e.key === 'ArrowLeft') {
      focusBox(index - 1);
    } else if (e.key === 'ArrowRight') {
      focusBox(index + 1);
    }
  };

  const handlePaste = (e) => {
    e.preventDefault();
    const pasted = e.clipboardData.getData('text').replace(/\D/g, '').slice(0, OTP_LENGTH);
    if (!pasted) return;

    const updated = Array(OTP_LENGTH).fill('');
    pasted.split('').forEach((char, i) => { updated[i] = char; });
    setDigits(updated);

    // Focus the box after the last pasted digit (or last box if all filled)
    focusBox(Math.min(pasted.length, OTP_LENGTH - 1));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const otp = digits.join('');
    if (otp.length < OTP_LENGTH) {
      setMessage('Please enter all 6 digits.');
      setStatus('error');
      focusBox(digits.findIndex(d => d === '')); // focus first empty box
      return;
    }

    // ── useRef usage 2 in action ─────────────────────────────────────────────
    // Prevent double-submit without useState (which would cause a re-render).
    // hasSubmitted.current update is instant and synchronous.
    if (hasSubmitted.current) return;
    hasSubmitted.current = true;

    setStatus('loading');
    setMessage('');

    try {
      // Simulate API call — replace with: await verifyOtp(otp)
      await new Promise((resolve, reject) =>
        setTimeout(() => (otp === '123456' ? resolve() : reject(new Error('Invalid OTP'))), 1200)
      );

      setStatus('success');
      setMessage('✅ OTP verified successfully!');
      onVerify?.(otp);

    } catch (err) {
      setStatus('error');
      setMessage('❌ ' + err.message + '. Try again.');
      hasSubmitted.current = false; // allow retry

      // Clear and refocus first box
      setDigits(Array(OTP_LENGTH).fill(''));
      // setTimeout so focus runs after state update re-render
      setTimeout(() => focusBox(0), 0);
    }
  };

  const handleResend = () => {
    setDigits(Array(OTP_LENGTH).fill(''));
    setStatus('idle');
    setMessage('');
    hasSubmitted.current = false;
    // focusBox after state settles
    setTimeout(() => focusBox(0), 0);
  };

  const isLoading = status === 'loading';
  const isSuccess = status === 'success';

  return (
    <div className="otp-wrapper">
      <div className="otp-card">
        <div className="otp-header">
          <span className="otp-icon">🔐</span>
          <h2>Enter OTP</h2>
          <p className="otp-sub">
            A 6-digit code was sent to your registered mobile number.
          </p>
          <p className="otp-hint">
            <em>(Hint: try <strong>123456</strong>)</em>
          </p>
        </div>

        <form onSubmit={handleSubmit} noValidate>
          {/* OTP digit boxes */}
          <div className="otp-boxes" onPaste={handlePaste}>
            {digits.map((digit, i) => (
              <input
                key={i}
                ref={el => (inputRefs.current[i] = el)}   // ← store DOM ref
                type="text"
                inputMode="numeric"
                maxLength={1}
                value={digit}
                onChange={e => handleChange(e, i)}
                onKeyDown={e => handleKeyDown(e, i)}
                onClick={() => inputRefs.current[i]?.select()} // select on click
                disabled={isLoading || isSuccess}
                className={`otp-box ${digit ? 'otp-box--filled' : ''} ${
                  status === 'error' ? 'otp-box--error' : ''
                } ${isSuccess ? 'otp-box--success' : ''}`}
                aria-label={`OTP digit ${i + 1}`}
                autoComplete="one-time-code"
              />
            ))}
          </div>

          {/* Status message */}
          {message && (
            <p className={`otp-message ${status === 'error' ? 'otp-message--error' : 'otp-message--success'}`}>
              {message}
            </p>
          )}

          {/* Actions */}
          {!isSuccess ? (
            <button
              type="submit"
              className="btn-primary"
              disabled={isLoading}
            >
              {isLoading ? 'Verifying…' : 'Verify OTP'}
            </button>
          ) : (
            <button type="button" className="btn-secondary" onClick={handleResend}>
              Enter New OTP
            </button>
          )}
        </form>

        {!isSuccess && (
          <p className="otp-resend">
            Didn't receive it?{' '}
            <button className="otp-resend-btn" type="button" onClick={handleResend}>
              Resend OTP
            </button>
          </p>
        )}
      </div>

      {/* Explanation panel */}
      <div className="otp-explanation">
        <h3>useRef in this component</h3>
        <div className="otp-ref-item">
          <code>inputRefs</code>
          <span>Array of DOM refs — one per digit box. Enables <code>.focus()</code> calls for auto-advance, backspace, paste, and error recovery without any re-renders.</span>
        </div>
        <div className="otp-ref-item">
          <code>hasSubmitted</code>
          <span>Mutable flag that persists across re-renders but does NOT cause one — prevents double-submit on fast clicks. A <code>useState</code> here would trigger an extra render for no UI reason.</span>
        </div>
        <div className="otp-rule">
          <strong>Rule of thumb:</strong> Use <code>useRef</code> when you need to either
          (1) touch the DOM directly, or (2) remember a value between renders that <em>shouldn't</em> trigger a re-render.
        </div>
      </div>
    </div>
  );
}
