@echo off
for /L %%i in (1, 1, 20) do (
    start cmd /k curl -d 1 http://localhost:8080/
)