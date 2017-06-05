#include <iostream>
#include "opencv2/opencv.hpp"

#include <CkRest.h>
#include <CkJsonObject.h>
#include <CkHttpRequest.h>
#include <CkHttp.h>
#include <CkHttpResponse.h>
#include <app.h>
#include <messaging.h>

using namespace cv;
using namespace std;
using namespace System;
using namespace System.Threading;


int main(int argc, const char * argv[]) {
	std::cout << "Empezando..\n";

	cv::namedWindow("Frames");
	cv::namedWindow("Contornos");

	//cv::VideoCapture capture("rtsp://root:axis@172.16.200.31/onvif-media/media.amp");
	cv::VideoCapture capture("rtsp://admin:jvc@88.2.40.161:25554/ONVIF/Streaming/channels/0");

	int frameIndex = 0;
	Mat lastFrame;
	cv::VideoWriter writer;
	bool detectado = false;
	
	while (capture.isOpened())     
	{
		cv::Mat frame;
		if (!capture.read(frame)) 
			break;

		#if 1
		Mat grayFrame, dilatedFrame, edges, deltaFrame, deltaCopyFrame;

		// Reescalar imagen
		cv::resize(frame, frame, Size(0, 0), 0.33, 0.33);

		// Convertir a escala de grises
		cv::cvtColor(frame, grayFrame, CV_BGR2GRAY);

		// Filtro Gaussian Blur -> eliminar pixeles sueltos
		cv::GaussianBlur(grayFrame, grayFrame, Size(21, 21), 0);

		if (frameIndex == 0) {
			frameIndex++;
			
			moveWindow("Frames", 0, 0);
			moveWindow("Contornos", 0, grayFrame.size().height);
			
			std::string filename = "/Users/ANDREA/Escritorio/capture.avi";
			int fcc = capture.get(CV_CAP_PROP_FOURCC);
			int fps = capture.get(CV_CAP_PROP_FPS);
			Size frameSize(grayFrame.size().width, grayFrame.size().height);
			writer = VideoWriter(filename, fcc, fps, frameSize);

			std::cout << "Tamaño frames = " << grayFrame.size().width << " x " << grayFrame.size().height << "\n";
			
			lastFrame = grayFrame;
			continue;
		}
		else if ((frameIndex % 50) == 0) {
			frameIndex = 0;
		}
		frameIndex++;


		//Diferencia entre imagenes
		cv::absdiff(lastFrame, grayFrame, deltaFrame);
		cv::threshold(deltaFrame, deltaFrame, 50, 255, cv::THRESH_BINARY);

		//Encontrar contornos
		int iterations = 2;
		cv::dilate(deltaFrame, deltaFrame, Mat(), Point(-1, -1), iterations);
		
		vector<vector<Point>> contours;
		vector<Vec4i> hierarchy;
		
		cv::findContours(deltaFrame, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));
		vector<vector<Point> > contours_poly(contours.size());
		vector<Rect> boundRect(contours.size());
		vector<Point2f>center(contours.size());
		vector<float>radius(contours.size());
		for (int i = 0; i < contours.size(); i++)
		{
			detectado = true;
			approxPolyDP(Mat(contours[i]), contours_poly[i], 3, true);
			boundRect[i] = boundingRect(Mat(contours_poly[i]));
			minEnclosingCircle((Mat)contours_poly[i], center[i], radius[i]);
			if (detectado)
			{
				std::cout << "Movimiento detectado." << endl;
				sendPostData();
				Thread.Sleep(9000);
				detectado = false;
			}			
		}


		//Linias y contornos para ver mejor la DM
		for (int i = 0; i< contours.size(); i++)
		{
			Scalar color = Scalar(255, 0, 0);
			drawContours(frame, contours_poly, i, color, 1, 8, vector<Vec4i>(), 0, Point());
			rectangle(frame, boundRect[i].tl(), boundRect[i].br(), color, 2, 8, 0);
			circle(frame, center[i], (int)radius[i], color, 2, 8, 0);
		}

		cv::imshow("Frames", frame);
		cv::imshow("Contornos", deltaFrame);
		#else
		imshow("Frame", frame);
		#endif

		writer.write(frame);

		switch (waitKey(1)) {
			case 27:
				capture.release();
				writer.release();
				return 0;
			}
	}
	return 0;

}

void sendPostData()
{
	CkHttp http;

	http.SetRequestHeader("Content-Type", "application/json");
	http.SetRequestHeader("Authorization", "key=AIzaSyAZOHnpX66qjNua6A2R6Y_ojn53gII4ZM0");

	const char *jsonText = "{'to' : 'cc6VGMjpIiA:APA91bGLpm5Z2p0NNh7nxttCTVd1tTsL2jObDaS9U8G1YjDjkpwkBlRLjU89ns4ujQ7rFU1Z2NshpUAX2RiQiIDKhHJdB0RtSS3H6nTT-lGEkIpzVtVzJpLIVqzSVbRjmyYlxD3BSLZl', 'data' : { 'Se ha detectado movimiento en una de sus cámaras'}, }";

	CkHttpResponse *resp = 0;
	resp = http.PostJson2("https://fcm.googleapis.com/fcm/send", "application/json", jsonText);
	if (resp == 0) {
		std::cout << http.lastErrorText() << "\r\n";
	}
}