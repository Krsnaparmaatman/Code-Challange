#!/bin/bash

# Define the target URL and number of requests
target_url="http://127.0.0.1:8888/"  # Replace with your server URL
num_requests=100

# Run Apache Benchmark with rate limiting
ab -n $num_requests -c 10 -g results.tsv "$target_url"
