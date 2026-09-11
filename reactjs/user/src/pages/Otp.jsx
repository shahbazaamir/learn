import OtpInput from '../components/OtpInput';

export default function Otp() {
  const handleVerified = (otp) => {
    console.log('OTP verified:', otp);
  };

  return <OtpInput onVerify={handleVerified} />;
}
