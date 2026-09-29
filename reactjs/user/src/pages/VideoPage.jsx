import VideoPlayer from '../components/VideoPlayer';

export default function VideoPage() {
  return (
    <div style={styles.page}>
      <h2 style={styles.heading}>🎬 Video on Demand</h2>
      <VideoPlayer src="/videos/claude.mov" title="claude.mov" />
    </div>
  );
}

const styles = {
  page: {
    padding: '32px 24px',
    minHeight: '100vh',
    background: '#141414',
  },
  heading: {
    color: '#fff',
    fontSize: '22px',
    fontWeight: 700,
    marginBottom: '24px',
    letterSpacing: '0.02em',
  },
};
