# Secure Comms Hub

A minimalist real-time communication platform featuring full-duplex messaging, authenticated End-to-End Encryption (E2EE), and low-latency Peer-to-Peer (P2P) voice and video calling.

## Architecture & Tech Stack

* **Control Plane (Backend):** Java 17 + Javalin (Embedded Jetty) handling WebSocket (`ws://`) routing. Sessions are managed concurrently in-memory without persisting any data.
* **Data Plane (Frontend):** Minimalist monochrome HTML5 client utilizing the native browser **Web Crypto API** (AES-GCM 256-bit keys) for text encryption.
* **Media Stream (Voice/Video):** Powered by **WebRTC (via PeerJS)**. Media streams establish direct client-to-client connections, completely bypassing the Java backend.

---
<img width="500" height="" alt="image" src="https://github.com/user-attachments/assets/93aa7a98-e034-4a71-8932-b9097acc0a67" />
