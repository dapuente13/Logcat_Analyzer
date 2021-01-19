#!/bin/bash
cat SuspiciousList/app*/* > data.txt
echo end >> data.txt
nc -l 1234 < data.txt

