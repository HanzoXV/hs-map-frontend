# HSMap

A voice-first, AI-powered navigation application designed for Pakistan. HSMap allows users to interact with a map naturally using voice commands in Urdu, English, or a code-switched mix of both (e.g., "mujhe IBA k pass jana hai"). The application parses conversational intent and entities to execute map actions such as navigation, location searching, and managing saved places entirely through speech.

## Features

### Core MVP
* **Voice Command Capture:** Real-time speech-to-text processing for multilingual speech inputs.
* **Conversational Intent Parsing:** Machine learning-driven extraction of intents (navigate, save, find nearby) and entities (locations, custom names).
* **Routing & Map Rendering:** Dynamic turn-by-turn navigation overlay.
* **Location Management:** Support for dedicated "Home" and "Work" shortcuts alongside custom-labeled saved coordinates.
* **Search History:** Persistent log of recent queries, targets, and routes.

### Future Roadmap
* Multi-stop route updates via voice ("go home, but stop for fuel first").
* Time and category-aware local search context.
* Voice-triggered ETA sharing over SMS or WhatsApp.
* Offline vector tile and routing engine caching.

## Architecture & Tech Stack

The system employs a thin client architecture, routing all heavy NLP and spatial logic through a centralized backend API to protect API keys and minimize mobile processing overhead.

### Frontend (Mobile Client)
* **Framework:** Native Android via Kotlin and Jetpack Compose.
* **Map Engine:** MapLibre with an open-source Compose wrapper rendering OSM-based vector tiles.
* **Speech Integration:** Azure AI Speech SDK for streaming speech-to-text natively optimizing South Asian dialect accuracy.

### Backend
* **Framework:** ASP.NET Core Web API with ASP.NET Core Identity authentication.
* **Database:** PostgreSQL with the PostGIS extension managing spatial data mapping (`GEOGRAPHY` types).
* **Routing & Geocoding:** Self-hosted OSRM (Open Source Routing Machine) and Nominatim/Photon containers using Pakistan map extracts from Geofabrik.
* **Natural Language Processing:** Gemini API (Flash-Lite model) utilized exclusively as a server-side JSON text classifier via structured function calling. 

## Database Schema Overview

The database consists of three primary tables:
1. **users:** Manages core accounts, authentication tokens, display names, and preferred application language defaults.
2. **saved_places:** Maps user-defined points of interest using spatial indexing (GiST index over PostGIS geography points). Enforces unique "home" and "work" fields per identity profile.
3. **search_history:** Stores structural execution logs (transcripts, extracted metadata targets, timestamps) to generate the recents cache.
