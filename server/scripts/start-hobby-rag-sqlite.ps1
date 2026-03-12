param(
    [string]$ChatModel = 'deepseek-r1:8b',
    [string]$EmbeddingModel = 'nomic-embed-text:latest'
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$serverRoot = Split-Path -Parent $PSScriptRoot
Set-Location $serverRoot

${env:APP_RAG_OLLAMA_CHAT_MODEL} = $ChatModel
${env:APP_RAG_OLLAMA_EMBEDDING_MODEL} = $EmbeddingModel

Write-Host "[RAG] starting server with sqlite profile and Ollama($ChatModel)..."
& .\gradlew.bat bootRun --args="--spring.profiles.active=sqlite"
