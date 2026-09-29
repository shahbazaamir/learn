import { useRef, useState, useCallback } from 'react';

export default function VideoPlayer({ src, title = 'Video' }) {
  const videoRef = useRef(null);
  const [playing, setPlaying] = useState(false);
  const [volume, setVolume] = useState(1);
  const [progress, setProgress] = useState(0);
  const [duration, setDuration] = useState(0);

  const togglePlay = useCallback(() => {
    const v = videoRef.current;
    if (!v) return;
    if (v.paused) {
      v.play();
      setPlaying(true);
    } else {
      v.pause();
      setPlaying(false);
    }
  }, []);

  const handleTimeUpdate = useCallback(() => {
    const v = videoRef.current;
    if (!v) return;
    setProgress(v.currentTime);
  }, []);

  const handleLoadedMetadata = useCallback(() => {
    setDuration(videoRef.current?.duration ?? 0);
  }, []);

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

  const handleEnded = useCallback(() => setPlaying(false), []);

  const fmt = (s) => {
    const m = Math.floor(s / 60);
    const sec = Math.floor(s % 60).toString().padStart(2, '0');
    return `${m}:${sec}`;
  };

  return (
    <div style={styles.wrapper}>
      <p style={styles.title}>{title}</p>

      {/* Video */}
      <div style={styles.videoBox}>
        <video
          ref={videoRef}
          src={src}
          style={styles.video}
          onTimeUpdate={handleTimeUpdate}
          onLoadedMetadata={handleLoadedMetadata}
          onEnded={handleEnded}
          onClick={togglePlay}
        />
        {/* Big play overlay when paused */}
        {!playing && (
          <button style={styles.overlay} onClick={togglePlay} aria-label="Play">
            ▶
          </button>
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
          type="range"
          min={0}
          max={duration || 0}
          step={0.1}
          value={progress}
          onChange={handleSeek}
          style={styles.seek}
          aria-label="Seek"
        />

        {/* Time */}
        <span style={styles.time}>{fmt(progress)} / {fmt(duration)}</span>

        {/* Volume icon */}
        <span style={styles.volIcon}>{volume === 0 ? '🔇' : volume < 0.5 ? '🔉' : '🔊'}</span>

        {/* Volume slider */}
        <input
          type="range"
          min={0}
          max={1}
          step={0.05}
          value={volume}
          onChange={handleVolume}
          style={styles.volSlider}
          aria-label="Volume"
        />
      </div>
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
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    background: 'rgba(0,0,0,0.55)',
    border: 'none',
    color: '#fff',
    fontSize: '48px',
    width: '80px',
    height: '80px',
    borderRadius: '50%',
    cursor: 'pointer',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    transition: 'background 0.2s',
  },
  controls: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    marginTop: '12px',
    padding: '8px 4px',
  },
  btn: {
    background: 'none',
    border: 'none',
    color: '#fff',
    fontSize: '20px',
    cursor: 'pointer',
    padding: '0 4px',
    flexShrink: 0,
  },
  seek: {
    flex: 1,
    accentColor: '#e50914',
    cursor: 'pointer',
    height: '4px',
  },
  time: {
    color: '#aaa',
    fontSize: '12px',
    whiteSpace: 'nowrap',
    flexShrink: 0,
    fontVariantNumeric: 'tabular-nums',
  },
  volIcon: {
    fontSize: '16px',
    flexShrink: 0,
  },
  volSlider: {
    width: '80px',
    accentColor: '#e50914',
    cursor: 'pointer',
    height: '4px',
    flexShrink: 0,
  },
};
