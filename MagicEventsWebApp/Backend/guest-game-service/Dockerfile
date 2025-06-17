FROM continuumio/miniconda3

WORKDIR /app

# Copia i file nella directory di lavoro
COPY tree.py .
COPY requirements.txt .

# Crea un ambiente conda e installa i pacchetti necessari
RUN conda create -n myenv python=3.10 \
    scikit-learn \
    fastapi \
    uvicorn \
    pandas \
    matplotlib \
    numpy \
    -y

# Imposta la shell per usare sempre l'ambiente conda
SHELL ["conda", "run", "-n", "myenv", "/bin/bash", "-c"]

# Esponi la porta per FastAPI
EXPOSE 8000

# Comando di avvio
CMD ["conda", "run", "-n", "myenv", "uvicorn", "tree:app", "--host", "0.0.0.0", "--port", "8000"]
