import { useRef, useState, useEffect, useCallback } from 'react';
import Hls from 'hls.js';

export default function HLSVideoPlayer({ src, title = 'Video' }) {
  const videoRef   = useRef(null);
  const hlsRef     = useRef(null);
  const [playing,  setPlaying]  = useState(false);
  const [volume,   setVolume]   = useState(1);
  const [progress, setProgress] = useState(0);
  const [duration, setDuration] = useState(0);
  const [levels,   setLevels]   = useState([]);   // HLS quality levels
  const [level,    setLevel]    = useState(-1);   // -1 = Auto
  const [error,    setError]    = useState(null);

  // Attach HLS.js (or native HLS on Safari)
  useEffect(() => {
    const video = videoRef.current;
    if (!video || !src) return;

    setError(null);

    if (Hls.isSupported()) {
      const hls = new Hls({
        enableWorker: true,
        lowLatencyMode: false,
      });
      hlsRef.current = hls;

      hls.loadSource(src);
      hls.attachMedia(video);

      hls.on(Hls.Events.MANIFEST_PARSED, (_, data) => {
        // Build quality level list from manifest
        setLevels(data.levels.map((l, i) => ({
          index: i,
          label: l.height ? `${l.height}p` : `Level ${i}`,
          bitrate: l.bitrate,
        })));
        setLevel(-1); // start on Auto
      });

      hls.on(Hls.Events.ERROR, (_, data) => {
        if (data.fatal) {
          setError(`HLS error: ${data.type} — ${data.details}`);
          hls.destroy();
        }
      });

      return () => {
        hls.destroy();
        hlsRef.current = null;
      };
    } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
      // Safari native HLS
      video.src = src;
    } else {
      setError('HLS is not supported in this browser.');
    }
  }, [src]);

  // Apply quality level selection
  useEffect(() => {
    if (hlsRef.current) {
      hlsRef.current.currentLevel = level;
    }
  }, [level]);

  const togglePlay = useCallback(() => {
    const v = videoRef.current;
    if (!v) return;
    if (v.paused) { v.play(); setPlaying(true); }
    else          { v.pause(); setPlaying(false); }
  }, []);

  const handleTimeUpdate   = useCallback(() => setProgress(videoRef.current?.currentTime ?? 0), []);
  const handleLoadedMeta   = useCallback(() => setDuration(videoRef.current?.duration ?? 0), []);
  const handleEnded        = useCallback(() => setPlaying(false), []);

  const handleSeek = useCallback((e) => {
    const v = videoRef.current;
    if (!v) return;
    const val = Number(e.target.value);
    v.currentTime = val;
    setProgress(val);
  }, []);

  const handleVolume = useCallback((e) => {
    const val = Number(e.target.value);
    if (videoRef.current) videoRef.current.volume = val;
    setVolume(val);
  }, []);

  const handleLevelChange = useCallback((e) => {
    setLevel(Number(e.target.value));
  }, []);

  const fmt = (s) => {
    if (!isFinite(s)) return '0:00';
    const m   = Math.floor(s / 60);
    const sec = Math.floor(s % 60).toString().padStart(2, '0');
    return `${m}:${sec}`;
  };

  return (
    <div style={styles.wrapper}>
      <p style={styles.title}>{title}</p>

      {error && <div style={styles.error}>{error}</div>}

      {/* Video */}
      <div style={styles.videoBox}>
        <video
          ref={videoRef}
          style={styles.video}
          onTimeUpdate={handleTimeUpdate}
          onLoadedMetadata={handleLoadedMeta}
          onEnded={handleEnded}
          onClick={togglePlay}
          playsInline
        />
        {!playing && !error && (
          <button style={styles.overlay} onClick={togglePlay} aria-label="Play">▶</button>
        )}
      </div>

      {/* Controls */}
      <div style={styles.controls}>

        {/* Play / Pause */}
        <button style={styles.btn} onClick={togglePlay} aria-label={playing ? 'Pause' : 'Play'}>
          {playing ? '⏸' : '▶'}
        </button>

        {/* Seek bar */}
        <input
          type="range" min={0} max={duration || 0} step={0.1} value={progress}
          onChange={handleSeek} style={styles.seek} aria-label="Seek"
        />

        {/* Time */}
        <span style={styles.time}>{fmt(progress)} / {fmt(duration)}</span>

        {/* Volume */}
        <span style={styles.volIcon}>{volume === 0 ? '🔇' : volume < 0.5 ? '🔉' : '🔊'}</span>
        <input
          type="range" min={0} max={1} step={0.05} value={volume}
          onChange={handleVolume} style={styles.volSlider} aria-label="Volume"
        />

        {/* Quality selector — only shown when HLS manifest has levels */}
        {levels.length > 0 && (
          <select
            value={level}
            onChange={handleLevelChange}
            style={styles.quality}
            aria-label="Quality"
          >
            <option value={-1}>Auto</option>
            {levels.map((l) => (
              <option key={l.index} value={l.index}>{l.label}</option>
            ))}
          </select>
        )}
      </div>

      {/* HLS badge */}
      <div style={styles.badge}>HLS</div>
    </div>
  );
}

const styles = {
  wrapper: {
    background: '#0f0f0f',
    borderRadius: '12px',
    padding: '20px',
    maxWidth: '860px',
    margin: '0 auto',
    boxShadow: '0 8px 32px rgba(0,0,0,0.5)',
    position: 'relative',
  },
  title: {
    color: '#e0e0e0',
    fontSize: '15px',
    fontWeight: 500,
    marginBottom: '12px',
    letterSpacing: '0.03em',
  },
  videoBox: {
    position: 'relative',
    background: '#000',
    borderRadius: '8px',
    overflow: 'hidden',
    cursor: 'pointer',
  },
  video: {
    width: '100%',
    display: 'block',
    maxHeight: '480px',
    objectFit: 'contain',
  },
  overlay: {
    position: 'absolute',
    top: '50%', left: '50%',
    transform: 'translate(-50%, -50%)',
    background: 'rgba(0,0,0,0.55)',
    border: 'none',
    color: '#fff',
    fontSize: '48px',
    width: '80px', height: '80px',
    borderRadius: '50%',
    cursor: 'pointer',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  controls: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    marginTop: '12px',
    padding: '8px 4px',
  },
  btn: {
    background: 'none', border: 'none',
    color: '#fff', fontSize: '20px',
    cursor: 'pointer', padding: '0 4px',
    flexShrink: 0,
  },
  seek: {
    flex: 1,
    accentColor: '#e50914',
    cursor: 'pointer',
    height: '4px',
  },
  time: {
    color: '#aaa', fontSize: '12px',
    whiteSpace: 'nowrap', flexShrink: 0,
    fontVariantNumeric: 'tabular-nums',
  },
  volIcon: { fontSize: '16px', flexShrink: 0 },
  volSlider: {
    width: '80px',
    accentColor: '#e50914',
    cursor: 'pointer',
    height: '4px',
    flexShrink: 0,
  },
  quality: {
    background: '#222', color: '#fff',
    border: '1px solid #444',
    borderRadius: '4px',
    padding: '2px 6px',
    fontSize: '12px',
    cursor: 'pointer',
    flexShrink: 0,
  },
  badge: {
    position: 'absolute',
    top: '16px', right: '16px',
    background: '#e50914',
    color: '#fff',
    fontSize: '10px',
    fontWeight: 700,
    padding: '2px 7px',
    borderRadius: '4px',
    letterSpacing: '0.08em',
  },
  error: {
    background: '#3a0a0a',
    border: '1px solid #e50914',
    color: '#ff6b6b',
    borderRadius: '6px',
    padding: '10px 14px',
    fontSize: '13px',
    marginBottom: '12px',
  },
};
