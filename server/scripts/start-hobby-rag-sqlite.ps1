param(
    [string]$ChatModel = 'deepseek-r1:8b',
    [string]$EmbeddingModel = 'nomic-embed-text:latest',
    [switch]$FastMode,
    [bool]$RerankEnabled = $true,
    [bool]$SummaryEnabled = $true
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$serverRoot = Split-Path -Parent $PSScriptRoot
Set-Location $serverRoot

${env:APP_RAG_OLLAMA_CHAT_MODEL} = $ChatModel
${env:APP_RAG_OLLAMA_EMBEDDING_MODEL} = $EmbeddingModel
${env:APP_RAG_PERFORMANCE_FAST_MODE} = $FastMode.IsPresent.ToString().ToLowerInvariant()
${env:APP_RAG_PERFORMANCE_RERANK_ENABLED} = $RerankEnabled.ToString().ToLowerInvariant()
${env:APP_RAG_PERFORMANCE_SUMMARY_ENABLED} = $SummaryEnabled.ToString().ToLowerInvariant()

Write-Host "[RAG] starting server with sqlite profile and Ollama($ChatModel), fastMode=$($FastMode.IsPresent), rerank=$RerankEnabled, summary=$SummaryEnabled ..."
& .\gradlew.bat bootRun --args="--spring.profiles.active=sqlite"
