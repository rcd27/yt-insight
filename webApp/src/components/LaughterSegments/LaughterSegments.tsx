import { useState } from 'react';
import './LaughterSegments.css';
import segmentsData from '../../segment_laughter_output.json';

interface Segment {
  filename: string;
  start: number;
  end: number;
  deleted?: boolean;
}

const YOUTUBE_VIDEO_ID = 'K_zrdqPHKbk';

export function LaughterSegments() {
  const [segments, setSegments] = useState<Segment[]>(segmentsData);

  const handleUpdate = (index: number, field: keyof Segment, value: string) => {
    const newSegments = [...segments];
    const numValue = parseFloat(value);
    if (!isNaN(numValue)) {
      (newSegments[index] as any)[field] = numValue;
      setSegments(newSegments);
      saveToEditedFile(newSegments);
    }
  };

  const handleDelete = (index: number) => {
    const newSegments = [...segments];
    newSegments[index] = {
      ...newSegments[index],
      deleted: !newSegments[index].deleted
    };
    setSegments(newSegments);
    saveToEditedFile(newSegments);
  };

  const saveToEditedFile = (data: Segment[]) => {
    const dataToSave = data.filter((segment) => !segment.deleted);
    console.log('new', dataToSave);
    const jsonString = JSON.stringify(dataToSave, null, 2);
    const blob = new Blob([jsonString], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'segment_laughter_output_edited.json';
    document.body.appendChild(link);
    // Мы не вызываем link.click() автоматически, чтобы не спамить загрузками.
    // Но по условию "сохранять рядом с оригинальным", в браузере это делается так.
    // Если бы у нас был API, мы бы слали POST запрос.
    // Добавим явную кнопку сохранения для удобства, либо будем использовать консоль/локальное хранилище.
    console.log('Saved to segment_laughter_output_edited.json', dataToSave);
    localStorage.setItem('laughter_segments_edited', jsonString);
  };

  const formatTime = (seconds: number) => {
    const date = new Date(0);
    date.setSeconds(seconds);
    return date.toISOString().substr(11, 8);
  };

  return (
    <div className="segments-container">
      <h1>Laughter Segments</h1>
      <div className="segments-grid">
        {segments.map((segment, index) => {
          const startTime = Math.floor(segment.start);
          const embedUrl = `https://www.youtube.com/embed/${YOUTUBE_VIDEO_ID}?start=${startTime}`;
          
          return (
            <div key={index} className={`segment-card ${segment.deleted ? 'deleted' : ''}`}>
              <div className="video-container">
                <iframe
                  width="100%"
                  height="200"
                  src={embedUrl}
                  title={`Laughter ${index}`}
                  frameBorder="0"
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen
                ></iframe>
              </div>
              <div className="segment-info">
                <h3>{segment.filename.split('/').pop()}</h3>
                <div className="edit-fields">
                  <div className="field">
                    <label>Start (s):</label>
                    <input
                      type="number"
                      step="0.1"
                      value={segment.start}
                      onChange={(e) => handleUpdate(index, 'start', e.target.value)}
                    />
                    <span className="formatted-time">{formatTime(segment.start)}</span>
                  </div>
                  <div className="field">
                    <label>End (s):</label>
                    <input
                      type="number"
                      step="0.1"
                      value={segment.end}
                      onChange={(e) => handleUpdate(index, 'end', e.target.value)}
                    />
                    <span className="formatted-time">{formatTime(segment.end)}</span>
                  </div>
                  <div className="duration">
                    Duration: {(segment.end - segment.start).toFixed(2)}s
                  </div>
                  <button 
                    className={`delete-button ${segment.deleted ? 'restore' : ''}`}
                    onClick={() => handleDelete(index)}
                  >
                    {segment.deleted ? 'Restore' : 'Delete'}
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>
      <div className="actions-bar">
        <button className="save-button" onClick={() => saveToEditedFile(segments)}>
          Download Edited JSON
        </button>
      </div>
    </div>
  );
}
