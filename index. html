<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CamBond</title>
<style>
*{box-sizing:border-box}
body{
 margin:0;background:#080d12;color:white;
 font-family:Arial,sans-serif;padding:25px 20px;
}
header{display:flex;align-items:center;gap:12px}
.logo{
 width:46px;height:46px;border-radius:14px;
 background:#153c32;color:#48e6a5;
 display:grid;place-items:center;font-size:25px;
}
h1{font-size:25px;margin:0}
small{color:#8c9b9a}
.hero{
 margin-top:35px;padding:25px 20px;
 border-radius:22px;
 background:linear-gradient(145deg,#173c32,#10221f);
}
.hero h2{font-size:27px;margin:0 0 12px}
.hero p{color:#b7c8c1;line-height:1.6}
.card{
 background:#141d24;border:1px solid #26343c;
 border-radius:18px;padding:20px;margin-top:18px;
}
.card h3{margin:0 0 10px}
button{
 border:0;border-radius:12px;padding:15px;
 width:100%;font-size:16px;font-weight:bold;
 background:#42e6a5;color:#092017;
 margin-top:12px;
}
.secondary{background:#26343c;color:white}
.status{color:#42e6a5;font-size:13px}
video{
 width:100%;margin-top:15px;border-radius:12px;
 background:#050505;display:none;
}
footer{text-align:center;color:#71817f;margin-top:35px;font-size:12px}
</style>
</head>
<body>

<header>
 <div class="logo">◉</div>
 <div>
  <h1>CamBond</h1>
  <small>Connect with permission.</small>
 </div>
</header>

<section class="hero">
 <h2>Your Camera,<br>Your Control.</h2>
 <p>Connect with people you trust.
 Share live video securely with your permission.</p>
 <div class="status">● Privacy First</div>
</section>

<section class="card">
 <h3>📹 Camera Sharing</h3>
 <p>Start a camera preview on this device.</p>
 <button onclick="startCamera()">Start Camera</button>
 <video id="preview" autoplay playsinline muted></video>
 <button class="secondary" onclick="stopCamera()">Stop Camera</button>
 <p id="cameraStatus" class="status">Camera is OFF</p>
</section>

<section class="card">
 <h3>🔐 My Connections</h3>
 <p>Manage your trusted connections.</p>
 <button class="secondary" onclick="alert('Connection system will be added next.')">
 Manage Connections
 </button>
</section>

<section class="card">
 <h3>🛡️ Security Center</h3>
 <p>Permission, privacy and access history.</p>
 <div class="status">● Your privacy matters</div>
</section>

<footer>
 CamBond © 2026<br>
 Privacy • Permission • Security
</footer>

<script>
let stream=null;

async function startCamera(){
 try{
  stream=await navigator.mediaDevices.getUserMedia({
   video:true,audio:false
  });
  const video=document.getElementById("preview");
  video.srcObject=stream;
  video.style.display="block";
  document.getElementById("cameraStatus").textContent=
   "● Camera is ON — local preview only";
 }catch(error){
  document.getElementById("cameraStatus").textContent=
   "Camera permission denied or unavailable.";
 }
}

function stopCamera(){
 if(stream){
  stream.getTracks().forEach(track=>track.stop());
  stream=null;
 }
 const video=document.getElementById("preview");
 video.srcObject=null;
 video.style.display="none";
 document.getElementById("cameraStatus").textContent="Camera is OFF";
}
</script>

</body>
</html>
