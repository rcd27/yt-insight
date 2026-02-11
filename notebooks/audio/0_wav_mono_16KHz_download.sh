#!/bin/bash

# Скрипт для быстрого скачивания аудио из видео в формате mono 16kHz
# Использование: ./yt-dlp-audio.sh <URL> [output_name]

set -e

# Проверка аргументов
if [ $# -lt 1 ]; then
    echo "Использование: $0 <URL> [output_name]"
    echo "Пример: $0 'https://youtube.com/watch?v=...' my_audio"
    exit 1
fi

URL="$1"
OUTPUT="${2:-audio}"

# Проверка наличия yt-dlp
if ! command -v yt-dlp &> /dev/null; then
    echo "Ошибка: yt-dlp не установлен"
    echo "Установите: pip install yt-dlp"
    exit 1
fi

# Проверка наличия ffmpeg
if ! command -v ffmpeg &> /dev/null; then
    echo "Ошибка: ffmpeg не установлен"
    echo "Установите: sudo apt install ffmpeg (или brew install ffmpeg на macOS)"
    exit 1
fi

echo "Скачивание аудио с: $URL"
echo "Формат: mono, 16kHz, WAV"
echo "---"

# Скачивание с оптимальными параметрами
yt-dlp \
    --proxy "localhost:8888" \
    --extract-audio \
    --audio-format wav \
    --audio-quality 0 \
    --postprocessor-args "ffmpeg:-ac 1 -ar 16000" \
    --concurrent-fragments 8 \
    --no-playlist \
    --no-warnings \
    --progress \
    -o "${OUTPUT}.%(ext)s" \
    "$URL"

echo "---"
echo "✓ Готово! Файл сохранён как: ${OUTPUT}.wav"
echo "Параметры: mono (1 канал), 16kHz sample rate"