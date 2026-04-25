#!/bin/bash
set -e

HBA_FILE="/var/lib/postgresql/data/pg_hba.conf"
ENTRY="host    all             all             0.0.0.0/0               scram-sha-256"

# Aguarda o arquivo existir (gerado pelo initdb na primeira execução)
until [ -f "$HBA_FILE" ]; do
  echo "Aguardando pg_hba.conf ser criado..."
  sleep 1
done

# Adiciona a entrada apenas se ainda não existir
if ! grep -qF "$ENTRY" "$HBA_FILE"; then
  echo "$ENTRY" >> "$HBA_FILE"
  echo "Entrada adicionada ao pg_hba.conf"
fi