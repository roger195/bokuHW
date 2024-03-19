# Boku SWE homework

---
This application sums up all the numbers it gets from post requests and returns the sum to 
all requests after it receives a post request with 'end'.

To run program:
```bash
docker-compose up
```

To test the program on a Windows machine you can use scripts to create 20 requests where each request sends the number
1 for a total of 20. Then you can run the 'end' script that sends the sum to all open terminals.
```bash
./createTerminals.bat
```
```bash
./endTerminal.bat
```