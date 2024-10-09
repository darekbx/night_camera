from http.server import BaseHTTPRequestHandler, HTTPServer
from picamera2 import Picamera2
from piservo import Servo
import RPi.GPIO as GPIO
import urllib.parse as urlparse
import time
import json
import base64

class ServoControl:

    _rotate_angle = 10
    _camera_angle = 0.1

    _rotate_servo = 12
    _camera_servo = 13

    _rotate = None
    _camera = None

    # initial positions
    _rotate_position = 90
    _camera_position = 92.7

    def __init__(self):
        self._rotate = Servo(self._rotate_servo, min_value=0, max_value=180)
        self._camera = Servo(self._camera_servo, min_value=92.2, max_value=93.2)

    def rotate_left(self):
        self._rotate_position = self._rotate_position + self._rotate_angle
        self._rotate.write(self._rotate_position)
        time.sleep(1)
        return self._rotate_position

    def rotate_right(self):
        self._rotate_position = self._rotate_position - self._rotate_angle
        self._rotate.write(self._rotate_position)
        time.sleep(1)
        return self._rotate_position

    def camera_left(self):
        self._camera_position = self._camera_position + self._camera_angle
        self._camera.write(self._camera_position)
        time.sleep(1)
        return self._camera_position

    def camera_right(self):
        self._camera_position = self._camera_position - self._camera_angle
        self._camera.write(self._camera_position)
        time.sleep(1)
        return self._camera_position
    
class CommandServer(BaseHTTPRequestHandler):

    _file_path = "/home/darek/photo.jpg"
    _camera = Picamera2()
    _servo_control = ServoControl()

    def do_GET(self):
        parsed_path = urlparse.urlparse(self.path)
        command = parsed_path.path.strip('/')
        query_params = urlparse.parse_qs(parsed_path.query)

        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()

        response = {'status': 'ok', 'command': command}
        
        if command == 'rotate_left':
            position = self._servo_control.rotate_left()
            response = {'status': 'ok', 'current_rotation': position}
        
        elif command == 'rotate_right':
            position = self._servo_control.rotate_right()
            response = {'status': 'ok', 'current_rotation': position}

        elif command == 'camera_left':
            position = self._servo_control.camera_left()
            response = {'status': 'ok', 'current_rotation': position}
        
        elif command == 'camera_right':
            position = self._servo_control.camera_right()
            response = {'status': 'ok', 'current_rotation': position}

        # Take a photo
        elif command == 'photo':
            exposure_time = query_params.get('exposure_time', [None])[0]
            if exposure_time:
                try:
                    exposure_time = float(exposure_time)
                    self.take_photo(exposure_time)
                    self.send_response(200)
                    self.send_header('Content-Type', 'text/plain')  # Sending as text/plain for simplicity
                    self.end_headers()
                    self.end_headers()

                    with open(self._file_path, 'rb') as file:
                        file_content = file.read()
                        encoded_content = base64.b64encode(file_content).decode('utf-8')
                        modified_content = f"--start--{encoded_content}--end--"

                        self.wfile.write(modified_content.encode('utf-8'))
                        return

                except ValueError:
                    response = {'status': 'error', 'message': 'Invalid exposure time'}
            else:
                response = {'status': 'error', 'message': 'Missing exposure time parameter'}

        else:
            response = {'status': 'error', 'message': 'Unknown command'}

        self.wfile.write(json.dumps(response).encode('utf-8'))

    def take_photo(self, exposure_time):
        self._camera.shutter_speed = int(exposure_time * 1_000_000)
        self._camera.iso = 800
        self._camera.focus = 0
        self._camera.awb_mode = 'auto'
        self._camera.exposure_mode = 'night'
        self._camera.resolution = (1920, 1080)
        self._camera.start()

        time.sleep(2)

        self._camera.capture_file(self._file_path)
        print(f"Photo taken and saved to {self._file_path}")

def run(server_class=HTTPServer, handler_class=CommandServer, port=8081):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print(f"Starting http server on port {port}...")
    httpd.serve_forever()

if __name__ == '__main__':
    run()

'''
@reboot /usr/bin/python3 /home/darek/server.py
'''