//#include <SoftwareSerial.h>
//SoftwareSerial BTserial(0, 1); // RX | TX
#include "DHT.h"
#define IN1 7
#define IN2 6
#define IN3 5
#define IN4 4
#define IN11 12
#define IN22 11
#define IN33 10
#define IN44 9
#define MAX_SPEED 255 //từ 0-255
#define MIN_SPEED 0
int sensorPin = A0;
int sensorValue = 0;
int tocDo = 0;
char lenh = ' ';
boolean xeChay = false;
const int DHTPIN = 3;
const int DHTTYPE = DHT11;
DHT dht(DHTPIN, DHTTYPE);
void nhietdo_doam() {
  float h = dht.readHumidity();    
  float t = dht.readTemperature(); 

  Serial.print(h);

  Serial.print(",");

  Serial.print(t);

  Serial.print(";");
  delay(1);
  //Serial.print("\n DO AM: ");
  //Serial.print(h);
  //Serial.print(" %\t");
  //Serial.print("NHIET DO: "); Serial.print(t); Serial.print(" *C ");
}
void setup() {
  Serial.begin(9600);
  //BTserial.begin(9600);
  dht.begin();
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);
  pinMode(IN3, OUTPUT);
  pinMode(IN4, OUTPUT);
  pinMode(IN11, OUTPUT);
  pinMode(IN22, OUTPUT);
  pinMode(IN33, OUTPUT);
  pinMode(IN44, OUTPUT);
}
void motor_Phai_Dung() {
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);
  digitalWrite(IN11, LOW);
  digitalWrite(IN22, LOW);
}
void motor_Trai_Dung() {
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, LOW);
  digitalWrite(IN33, LOW);
  digitalWrite(IN44, LOW);
}
void motor_Phai_Tien(int speed) { //speed: từ 0 - MAX_SPEED
  speed = constrain(speed, MIN_SPEED, MAX_SPEED);//đảm báo giá trị nằm trong một khoảng từ 0 - MAX_SPEED - http://arduino.vn/reference/constrain
  digitalWrite(IN1, HIGH);// chân này không có PWM
  analogWrite(IN2, 255 - speed);
  digitalWrite(IN11, LOW);// chân này không có PWM
  analogWrite(IN22, speed);
}
void motor_Phai_Lui(int speed) {
  speed = constrain(speed, MIN_SPEED, MAX_SPEED);//đảm báo giá trị nằm trong một khoảng từ 0 - MAX_SPEED - http://arduino.vn/reference/constrain
  digitalWrite(IN1, LOW);// chân này không có PWM
  analogWrite(IN2, speed);
  digitalWrite(IN11, HIGH);// chân này không có PWM
  analogWrite(IN22, 255 - speed);
}
void motor_Trai_Tien(int speed) { //speed: từ 0 - MAX_SPEED
  speed = constrain(speed, MIN_SPEED, MAX_SPEED);//đảm báo giá trị nằm trong một khoảng từ 0 - MAX_SPEED - http://arduino.vn/reference/constrain
  analogWrite(IN3, speed);
  digitalWrite(IN4, LOW);// chân này không có PWM
  analogWrite(IN33, speed);
  digitalWrite(IN44, LOW);// chân này không có PWM
}
void motor_Trai_Lui(int speed) {
  speed = constrain(speed, MIN_SPEED, MAX_SPEED);//đảm báo giá trị nằm trong một khoảng từ 0 - MAX_SPEED - http://arduino.vn/reference/constrain
  analogWrite(IN4, 255);
  digitalWrite(IN3, LOW);// chân này không có PWM
  analogWrite(IN44, 255);
  digitalWrite(IN33, LOW);// chân này không có PWM
}
void loop() {
  if (Serial.available()) {
    lenh = Serial.read();
  }
  if (lenh == 'X') {
    tocDo = 0;
    //Serial.println("X");
  } else tocDo += 3;
  switch (lenh) {
    case 'A':
      motor_Phai_Tien(tocDo);
      motor_Trai_Tien(tocDo);
      xeChay = true;
      //Serial.println("A");
      break;
    case 'B':
      motor_Phai_Lui(tocDo);
      motor_Trai_Lui(tocDo);
      xeChay = true;
      //Serial.println("B");
      break;
    case 'C':
      motor_Phai_Tien(tocDo);
      motor_Trai_Dung();
      xeChay = true;
      //Serial.println("C");
      break;
    case 'D':
      motor_Phai_Dung();
      motor_Trai_Tien(tocDo);
      xeChay = true;
      //Serial.println("D");
      break;
    case 'X':
      motor_Phai_Dung();
      motor_Trai_Dung();
      xeChay = true;
      //Serial.println("X");
      break;
  }
  nhietdo_doam();
}
