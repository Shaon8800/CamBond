<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CamBond</title>
<script src="https://unpkg.com/peerjs@1.5.5/dist/peerjs.min.js"></script>
<style>
*{box-sizing:border-box}
body{margin:0;background:#10131d;color:#fff;font-family:Arial,sans-serif;padding:22px}
.container{max-width:480px;margin:auto}
h1{text-align:center;color:#65e6c2;font-size:34px}
.sub{text-align:center;color:#aab3c5}
.card{background:#1c2231;padding:18px;border-radius:16px;margin:18px 0}
button{width:100%;padding:14px;border:0;border-radius:10px;background:#65e6c2;color:#10131d;font-weight:bold;font-size:16px;margin-top:10px}
input{width:100%;padding:14px;border-radius:9px;border:1px solid #46516a;background:#10131d;color:white;margin-top:10px}
video{width:100%;background:#080a10;border-radius:12px;margin-top:12px}
.status{color:#65e6c2;text-align:center;min-height:24px}
.small{font-size:12px;color:#aab3c5;line-height:1.5}
</style>
</head>
<body>
<div class="container">
<h1>CamBond</h1>
<p class="sub">Secure Live Camera Sharing</p>

<div class="card">
<h3>Share Your Camera</h3>
<p class="small">Start your camera and share your ID with the person you trust.</p>
<button id="start">Start Camera & Share</button>
<p>Your Connection ID:</p>
<input id="myId" readonly placeholder="Your ID will appear here">
<button id="copy">Copy My ID</button>
<video id="localVideo" autoplay muted playsinline></video>
</div>

<div class="card">
<h3>Connect to Another Phone</h3>
<input id="remoteId" placeholder="Enter the other phone's ID">
<button id="connect">Connect</button>
<video id="remoteVideo" autoplay playsinline controls></video>
</div>

<div class="card">
<button id="stop" style="background:#ff647c">Stop / Disconnect</button>
<p class="status" id="status">Ready</p>
<p class="small">Camera sharing requires the camera owner's permission. The owner can stop sharing at any time.</p>
</div>
</div>

<script>
let peer=null;
let localStream=null;
let activeCall=null;

const statusText=document.getElementById("status");
const localVideo=document.getElementById("localVideo");
const remoteVideo=document.getElementById("remoteVideo");
const myId=document.getElementById("myId");

function status(msg){
  statusText.textContent=msg;
}

function showRemote(stream){
  remoteVideo.srcObject=stream;
  remoteVideo.play().catch(()=>{});
  status("Live connection established!");
}

document.getElementById("start").onclick=async()=>{
  try{
    if(peer) peer.destroy();
    status("Requesting camera permission...");
    localStream=await navigator.mediaDevices.getUserMedia({
      video:true,audio:true 
    });
    localVideo.srcObject=localStream;

    peer=new Peer();

    peer.on("open",id=>{
      myId.value=id;
      status("Camera ready. Share your ID.");
    });

    peer.on("call",call=>{
      const accepted=confirm(
        "Someone wants to view your camera. Allow this connection?"
      );
      if(!accepted){
        call.close();
        status("Connection declined.");
        return;
      }
      activeCall=call;
      call.answer(localStream);
      call.on("stream",showRemote);
      call.on("close",()=>status("Connection ended."));
      call.on("error",e=>status("Call error: "+e.message));
      status("You accepted the connection.");
    });

    peer.on("error",e=>status("Connection error: "+e.type));
  }catch(e){
    status("Camera error: "+e.message);
  }
};

document.getElementById("connect").onclick=async()=>{
  const id=document.getElementById("remoteId").value.trim();
  if(!id){status("Enter the other phone's ID.");return;}
  try{
    if(peer) peer.destroy();
    status("Requesting your camera permission...");
    localStream=await navigator.mediaDevices.getUserMedia({
      video:true,audio:false
    });
    localVideo.srcObject=localStream;

    peer=new Peer();
    peer.on("open",()=>{
      status("Calling the other phone...");
      activeCall=peer.call(id,localStream);
      if(!activeCall){
        status("Could not start call.");
        return;
      }
      activeCall.on("stream",showRemote);
      activeCall.on("close",()=>status("Connection ended."));
      activeCall.on("error",e=>status("Call error: "+e.message));
    });
    peer.on("error",e=>status("Connection error: "+e.type));
  }catch(e){
    status("Camera or connection error: "+e.message);
  }
};

document.getElementById("copy").onclick=async()=>{
  if(!myId.value){status("Start your camera first.");return;}
  try{
    await navigator.clipboard.writeText(myId.value);
    status("Connection ID copied!");
  }catch(e){
    myId.select();
    status("Select and copy your ID.");
  }
};

document.getElementById("stop").onclick=()=>{
  if(activeCall){activeCall.close();activeCall=null;}
  if(peer){peer.destroy();peer=null;}
  if(localStream){
    localStream.getTracks().forEach(track=>track.stop());
    localStream=null;
  }
  localVideo.srcObject=null;
  remoteVideo.srcObject=null;
  myId.value="";
  status("Sharing stopped. Camera disconnected.");
};
</script>
</body>
</html>