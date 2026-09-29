import HLSVideoPlayer from '../components/HLSVideoPlayer';

// Replace this URL with any .m3u8 HLS stream URL.
// Examples:
//   Apple test stream  : https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8
//   Cloudflare demo    : https://customer-f33zs165nr7gyfy4.cloudflarestream.com/6b9e68b07dfee8cc2d0bac763aa0aea7/manifest/video.m3u8
const HLS_SRC =
  'https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8';

export default function HLSVideoPage() {
  return (
    <div style={styles.page}>
      <h2 style={styles.heading}>📡 HLS Video on Demand</h2>
      <p style={styles.sub}>
        Streaming via HTTP Live Streaming (HLS) · powered by{' '}
        <a href="https://github.com/video-dev/hls.js" target="_blank" rel="noreferrer" style={styles.link}>
          hls.js
        </a>
      </p>

      <HLSVideoPlayer src={HLS_SRC} title="Apple Bipbop (HLS demo)" />

      <div style={styles.hint}>
        <strong style={{ color: '#e0e0e0' }}>To use your own HLS stream:</strong>
        <ol style={styles.ol}>
          <li>Convert your <code style={styles.code}>.mov</code> to HLS with ffmpeg:</li>
          <li>
            <code style={styles.codeBlock}>
              ffmpeg -i /path/to/claude.mov \<br />
              &nbsp;&nbsp;-codec: copy -start_number 0 \<br />
              &nbsp;&nbsp;-hls_time 6 -hls_list_size 0 \<br />
              &nbsp;&nbsp;-f hls /path/to/output/stream.m3u8
            </code>
          </li>
          <li>Serve the output folder and update <code style={styles.code}>HLS_SRC</code> in this file.</li>
        </ol>
      </div>
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
    marginBottom: '6px',
    letterSpacing: '0.02em',
  },
  sub: {
    color: '#888',
    fontSize: '13px',
    marginBottom: '24px',
  },
  link: {
    color: '#e50914',
    textDecoration: 'none',
  },
  hint: {
    marginTop: '28px',
    background: '#1a1a1a',
    border: '1px solid #2a2a2a',
    borderRadius: '8px',
    padding: '18px 20px',
    color: '#888',
    fontSize: '13px',
    lineHeight: 1.7,
  },
  ol: {
    margin: '8px 0 0 0',
    paddingLeft: '20px',
    color: '#888',
  },
  code: {
    background: '#2a2a2a',
    color: '#e0e0e0',
    padding: '1px 5px',
    borderRadius: '3px',
    fontFamily: 'monospace',
    fontSize: '12px',
  },
  codeBlock: {
    display: 'block',
    background: '#2a2a2a',
    color: '#b5e853',
    padding: '10px 14px',
    borderRadius: '5px',
    fontFamily: 'monospace',
    fontSize: '12px',
    margin: '6px 0',
    lineHeight: 1.6,
  },
};
