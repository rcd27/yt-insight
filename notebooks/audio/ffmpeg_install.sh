#!/bin/bash

# Скрипт для установки ffmpeg и ffprobe

echo "Установка ffmpeg и ffprobe..."
echo ""

# Определение ОС
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    echo "Обнаружена Linux система"

    # Debian/Ubuntu
    if command -v apt-get &> /dev/null; then
        echo "Установка через apt..."
        sudo apt-get update
        sudo apt-get install -y ffmpeg

    # Fedora/RHEL/CentOS
    elif command -v dnf &> /dev/null; then
        echo "Установка через dnf..."
        sudo dnf install -y ffmpeg

    # Arch Linux
    elif command -v pacman &> /dev/null; then
        echo "Установка через pacman..."
        sudo pacman -S ffmpeg

    else
        echo "Не удалось определить пакетный менеджер"
        echo "Установите ffmpeg вручную для вашего дистрибутива"
    fi

elif [[ "$OSTYPE" == "darwin"* ]]; then
    echo "Обнаружена macOS"

    if command -v brew &> /dev/null; then
        echo "Установка через Homebrew..."
        brew install ffmpeg
    else
        echo "Homebrew не установлен!"
        echo "Установите Homebrew: /bin/bash -c \"\$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)\""
        echo "Затем выполните: brew install ffmpeg"
    fi

else
    echo "Неизвестная ОС: $OSTYPE"
    echo "Скачайте ffmpeg с https://ffmpeg.org/download.html"
fi

echo ""
echo "Проверка установки..."
if command -v ffmpeg &> /dev/null && command -v ffprobe &> /dev/null; then
    echo "✓ ffmpeg установлен: $(ffmpeg -version | head -n1)"
    echo "✓ ffprobe установлен: $(ffprobe -version | head -n1)"
else
    echo "✗ Установка не завершена"
fi