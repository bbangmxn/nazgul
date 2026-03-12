param(
    [string]$BaseUrl = 'http://localhost:8080',
    [long]$HobbyId = 10,
    [string]$Query = '러닝 초보 루틴',
    [int]$Limit = 5,
    [switch]$Benchmark
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8
Add-Type -AssemblyName System.Net.Http

$httpClient = [System.Net.Http.HttpClient]::new()
$httpClient.Timeout = [TimeSpan]::FromSeconds(180)

function Invoke-JsonGet {
    param([string]$Url)
    $response = $httpClient.GetAsync($Url).GetAwaiter().GetResult()
    $response.EnsureSuccessStatusCode() | Out-Null
    $bytes = $response.Content.ReadAsByteArrayAsync().GetAwaiter().GetResult()
    $json = [System.Text.Encoding]::UTF8.GetString($bytes)
    return $json | ConvertFrom-Json
}

try {
    Invoke-JsonGet "$BaseUrl/actuator/health" | Out-Null
} catch {
    Write-Error "서버가 떠 있지 않습니다. 먼저 .\scripts\start-hobby-rag-sqlite.ps1 을 실행하세요."
}

if ($Benchmark) {
    $benchmark = Invoke-JsonGet "$BaseUrl/api/v1/optimization/rag/benchmark?hobbyId=$HobbyId&iterations=1"
    Write-Host "`n=== Benchmark ==="
    $benchmark | ConvertTo-Json -Depth 6
    return
}

$search = Invoke-JsonGet "$BaseUrl/api/v1/optimization/rag/search?hobbyId=$HobbyId&query=$([uri]::EscapeDataString($Query))&limit=$Limit"
$recommend = Invoke-JsonGet "$BaseUrl/api/v1/optimization/rag/recommend?hobbyId=$HobbyId&query=$([uri]::EscapeDataString($Query))&limit=$Limit"

Write-Host "`n=== Search ==="
$search | ConvertTo-Json -Depth 6

Write-Host "`n=== Recommend ==="
$recommend | ConvertTo-Json -Depth 6
