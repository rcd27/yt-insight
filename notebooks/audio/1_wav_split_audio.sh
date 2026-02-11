#!/bin/bash

# Продвинутый скрипт для разбивки аудио-файла
# Поддерживает разбивку по количеству частей или по времени

set -e

show_help() {
    cat << EOF
Использование: $0 <audio_file> [опции]

Опции:
    -n, --parts NUM        Разбить на NUM частей (по умолчанию: 4)
    -d, --duration SEC     Разбить на части по SEC секунд каждая
    -o, --output DIR       Папка для сохранения (по умолчанию: текущая)
    -h, --help            Показать эту справку

Примеры:
    $0 audio.wav -n 4                    # Разбить на 4 части
    $0 audio.wav -d 300                  # Части по 5 минут (300 сек)
    $0 audio.wav -n 3 -o chunks          # 3 части в папку chunks/

EOF
    exit 0
}

# Параметры по умолчанию
NUM_PARTS=4
CHUNK_DURATION=""
OUTPUT_DIR="."

# Парсинг аргументов
AUDIO_FILE=""
while [[ $# -gt 0 ]]; do
    case $1 in
        -n|--parts)
            NUM_PARTS="$2"
            shift 2
            ;;
        -d|--duration)
            CHUNK_DURATION="$2"
            shift 2
            ;;
        -o|--output)
            OUTPUT_DIR="$2"
            shift 2
            ;;
        -h|--help)
            show_help
            ;;
        *)
            if [ -z "$AUDIO_FILE" ]; then
                AUDIO_FILE="$1"
            fi
            shift
            ;;
    esac
done

# Проверки
if [ -z "$AUDIO_FILE" ]; then
    echo "Ошибка: не указан аудио-файл"
    show_help
fi

if [ ! -f "$AUDIO_FILE" ]; then
    echo "Ошибка: файл '$AUDIO_FILE' не найден"
    exit 1
fi

if ! command -v ffmpeg &> /dev/null; then
    echo "Ошибка: ffmpeg не установлен"
    exit 1
fi

# Создание выходной папки
mkdir -p "$OUTPUT_DIR"

# Получение информации о файле
DURATION=$(ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "$AUDIO_FILE")
FILENAME=$(basename "$AUDIO_FILE")
BASENAME="${FILENAME%.*}"
EXTENSION="${FILENAME##*.}"

echo "Файл: $AUDIO_FILE"
echo "Общая длительность: $(printf '%.2f' $DURATION) секунд ($(echo "$DURATION / 60" | bc -l | xargs printf '%.1f') минут)"

# Определение режима разбивки
if [ -n "$CHUNK_DURATION" ]; then
    # Режим по длительности
    NUM_PARTS=$(echo "($DURATION / $CHUNK_DURATION) + 0.99" | bc | cut -d'.' -f1)
    PART_DURATION=$CHUNK_DURATION
    echo "Режим: разбивка по $CHUNK_DURATION секунд"
else
    # Режим по количеству частей
    PART_DURATION=$(echo "$DURATION / $NUM_PARTS" | bc -l)
    echo "Режим: разбивка на $NUM_PARTS равных частей"
fi

echo "Длительность части: $(printf '%.2f' $PART_DURATION) секунд"
echo "Количество частей: $NUM_PARTS"
echo "---"

# Разбивка
for ((i=0; i<$NUM_PARTS; i++)); do
    START=$(echo "$i * $PART_DURATION" | bc -l)
    PART_NUM=$((i + 1))
    OUTPUT="${OUTPUT_DIR}/${BASENAME}_part${PART_NUM}.${EXTENSION}"

    printf "Часть %2d/%d: " "$PART_NUM" "$NUM_PARTS"

    ffmpeg -i "$AUDIO_FILE" \
        -ss "$START" \
        -t "$PART_DURATION" \
        -c copy \
        -y \
        -loglevel error \
        "$OUTPUT" 2>&1

    if [ $? -eq 0 ]; then
        SIZE=$(ls -lh "$OUTPUT" | awk '{print $5}')
        echo "✓ $OUTPUT ($SIZE)"
    else
        echo "✗ Ошибка при создании $OUTPUT"
    fi
done

echo "---"
echo "✓ Разбивка завершена!"
echo "Файлы сохранены в: $OUTPUT_DIR/"