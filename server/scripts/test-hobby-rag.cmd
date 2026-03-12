@echo off
setlocal
chcp 65001 > nul
powershell -ExecutionPolicy Bypass -File "%~dp0test-hobby-rag.ps1" %*
endlocal
