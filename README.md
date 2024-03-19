# Boku SWE homework
This application sums up all the numbers it gets from post requests and returns the sum to 
all requests after it receives a post request with 'end'.

To run program:
```bash
docker-compose up
```
---

## Functionality
Send numbers to be added to the total sum
```bash
curl -d 5 http://localhost:8080/
```
Send 'end' keyword for the program to respond to all open terminals with the current sum
```bash
curl -d end http://localhost:8080/
```
---

## Testing
To test the program on a Windows machine you can use scripts to create 20 requests where each request sends the number
1 for a total of 20. Then you can run the 'end' script that sends the sum to all open terminals.
```bash
./createTerminals.bat
```
```bash
./endTerminal.bat
```
---