#!/bin/bash
socat TCP-LISTEN:1234,fork,reuseaddr TCP-LISTEN:9999

