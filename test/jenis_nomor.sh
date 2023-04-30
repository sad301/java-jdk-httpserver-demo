#!/bin/bash

curl -v \
    -H "Content-Type: application/json" \
    -d "{\"id\":\"NIK\",\"deskripsi\":\"Nomor Induk Kependudukan\"}" \
    http://127.0.0.1:5000/api/jenis_nomor