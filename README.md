# read2understand

Tool to summarize highlights from Kindle books.

## Running locally

To start the tool:

1. [Install Ollama](https://ollama.com/download).
2. Run Ollama: `ollama run llama3.1`
3. Run the tool: `./gradlew bootRun`
4. Open your browser at <http://localhost:8080>.

To get a summary of Kindle highlights:

1. In Kindle, open your notes and tap the export button.
2. In you email app, open the email Kindle sent you and save the highlights PDF as a file.
3. In the tool UI, open the PDF and click `Upload`.
