param(
    [string]$BaseUrl = 'http://localhost:8080',
    [long]$HobbyId = 10,
    [string]$Query = '러닝 초보 루틴',
    [int]$Limit = 5,
    [switch]$Benchmark,
    [int]$TimeoutSec = 300,
    [switch]$SkipServerCheck
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8
Add-Type -AssemblyName System.Net.Http

$httpClient = [System.Net.Http.HttpClient]::new()
$httpClient.Timeout = [TimeSpan]::FromSeconds($TimeoutSec)

function Invoke-JsonGet {
    param([string]$Url)
    $response = $httpClient.GetAsync($Url).GetAwaiter().GetResult()
    $response.EnsureSuccessStatusCode() | Out-Null
    $bytes = $response.Content.ReadAsByteArrayAsync().GetAwaiter().GetResult()
    $json = [System.Text.Encoding]::UTF8.GetString($bytes)
    return $json | ConvertFrom-Json
}

function Test-ServerPort {
    param([string]$Url)

    try {
        $uri = [System.Uri]$Url
        $tcpClient = [System.Net.Sockets.TcpClient]::new()
        $asyncResult = $tcpClient.BeginConnect($uri.Host, $uri.Port, $null, $null)
        $connected = $asyncResult.AsyncWaitHandle.WaitOne(3000, $false)

        if (-not $connected) {
            $tcpClient.Close()
            return $false
        }

        $tcpClient.EndConnect($asyncResult)
        $tcpClient.Close()
        return $true
    } catch {
        return $false
    }
}

if (-not $SkipServerCheck) {
    if (-not (Test-ServerPort $BaseUrl)) {
        Write-Error "서버 포트가 열려 있지 않습니다. 먼저 .\scripts\start-hobby-rag-sqlite.ps1 을 실행하세요."
    }
}

if ($Benchmark) {
    $benchmarkResult = Invoke-JsonGet "$BaseUrl/api/v1/optimization/rag/benchmark?hobbyId=$HobbyId&iterations=1"
    Write-Host "`n=== Benchmark ==="
    $benchmarkResult | ConvertTo-Json -Depth 6
    return
}

$search = Invoke-JsonGet "$BaseUrl/api/v1/optimization/rag/search?hobbyId=$HobbyId&query=$([uri]::EscapeDataString($Query))&limit=$Limit"
$recommend = Invoke-JsonGet "$BaseUrl/api/v1/optimization/rag/recommend?hobbyId=$HobbyId&query=$([uri]::EscapeDataString($Query))&limit=$Limit"

Write-Host "`n=== Search ==="
$search | ConvertTo-Json -Depth 6

Write-Host "`n=== Recommend ==="
$recommend | ConvertTo-Json -Depth 6
