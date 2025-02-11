# AiTalks: Dual-AI Chat Application (OpenAI & Gemini)

**GeoSid**

## Overview

ChatGPTCompose_Lite is an innovative Android chat application that allows users to interact with two powerful AI language models: OpenAI and Google's Gemini (formerly Bard). This project explores the fascinating possibilities of AI interaction by creating a unique conversation flow where the responses from each AI model are fed as input to the other, creating a dynamic and often surprising dialogue.

## Key Features

*   **Dual-AI Interaction:** Seamlessly chat with both OpenAI and Gemini within a single app.
*   **AI-to-AI Conversation Flow:** Experience a unique conversation flow where OpenAI and Gemini "talk" to each other, building on each other's responses.
*   **"Yes or No" Game:** Engage the AI models in a fun and challenging "Yes or No" game, where Gemini must answer questions without using the words "yes" or "no."
*   **Streaming Responses:** Enjoy a more dynamic chat experience with streaming responses from the AI models.
*   **Modern Android Development:** Built using Kotlin, Jetpack Compose, ViewModel, StateFlow, Hilt, Coroutines, and Flow.
*   **KSP:** Use KSP for code generation.
*   **Clean Architecture:** Follows a clean architecture pattern for better code organization and maintainability.
*   **Error Handling:** The app has error handling to gracefully manage issues like network problems or API errors.
*   **Conversation Flow:** The app has a conversation flow where the response from OpenAI is sent to Gemini and vice versa.
*   **Prompts:** The app has a `systemPrompt` and `assistantPrompt` that are sent to the AI platforms to guide the conversation.
*   **Learning:** The app can be used to learn how to use AI platforms.

## Technologies Used

*   **Kotlin:** The primary programming language.
*   **Jetpack Compose:** For building the UI.
*   **ViewModel:** For managing UI-related data.
*   **StateFlow:** For managing and observing UI state changes.
*   **Hilt:** For dependency injection.
*   **Coroutines:** For asynchronous operations.
*   **Flow:** For handling streams of data.
*   **OpenAI API:** For interacting with OpenAI's language models.
*   **Gemini API:** For interacting with Google's Gemini language models.
*   **KSP:** For code generation.
*   **Gradle:** For build automation.

## Getting Started

### Prerequisites

*   Android Studio (latest version recommended)
*   Android SDK (API level 26 or higher)
*   OpenAI API Key
*   Gemini API Key

### Installation

1.  **Clone the repository:**
    *   bash git clone https://github.com/GeoSid/AiTalks

2.  **Open the project in Android Studio.**
3.  **Add API Keys:**
    *   Add your OpenAI and Gemini API keys to the `secret.properties` file:
4.  **Sync Project with Gradle Files:**
    *   Click "Sync Now" in the notification bar or go to `File > Sync Project with Gradle Files`.
5.  **Set Prompts:**
    *   You should set ASSISTANT_OPENAI_PROMPT- SYSTEM_OPENAI_PROMPT  and SYSTEM_GEMINI_PROMPT in the `Constant.kt` file.
    *   Try many aspects of the prompt to see what works best for you.
6.  **Build and Run:**
    *   Click the "Run" button in Android Studio

## Project Structure

The project follows a clean architecture pattern, with the following key modules:

*   **`app`:** The main application module.
*   **`data`:** Contains the data layer, including network and repository implementations.
*   **`domain`:** Contains the business logic and use cases.
*   **`di`:** Contains the dependency injection modules.
*   **`ui`:** Contains the UI components.
* 
## Video

Check the video [test_compose_ai_talks](https://github.com/GeoSId/AiTalks/blob/master/test_ai_chat.mp4)

## License

[MIT License](LICENSE)

## Contact

*   **GeoSid** - [LinkedIn](https://www.linkedin.com/in/george-sideris-5b8744b2/) -