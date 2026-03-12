@echo off
setlocal
chcp 65001 > nul
powershell -ExecutionPolicy Bypass -File "%~dp0start-hobby-rag-sqlite.ps1" %*
endlocal
