package com.example26.orange02.ex26_weatherxml;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> cities;
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    ArrayList<Weather> weathers; //날씨 정보를 저장할 리스트

    //이벤트가 발생하면 자동으로 호출되는 콜백함수
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0) {
                adapter.notifyDataSetChanged(); //리스트뷰 새로고침
                progressDialog.dismiss();
            }
            else if(msg.what==1){
                Toast.makeText(MainActivity.this,"다운로드 에러",Toast.LENGTH_SHORT).show();
            }

        }
    };

    //쓰레드 내부클래스:데이타를 다운로드해서 xml을 파싱하기위한 쓰레드
    class ThreadEx extends Thread
    {
        @Override
        public void run() {
            super.run();
            //xml dom 파싱을 위한 객체 생성
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder documentBuilder=factory.newDocumentBuilder();

                //파싱을 할 url 연결
                Document document=documentBuilder.parse("http://www.weather.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108");


                //루트에 대한 포인터 가져오기
                Element root=document.getDocumentElement();

                //city 태그 전부 가져오기
                NodeList items=root.getElementsByTagName("city");
//                NodeList items2=root.getElementsByTagName("tmx");
//                NodeList items3=root.getElementsByTagName("tmn");
//                NodeList items4=root.getElementsByTagName("wf");

                //ArrayList 초기화
                cities.clear();
//                weathers.clear();

                //city 갯수만큼 반복
                Log.i("info","시티갯수"+items.getLength());
                for(int i=0;i<items.getLength();i++)
                {
                    //item 한개 정보 얻기
                    Node item=items.item(i);
                    //태그 안의 내용 가져오기
                    Node city=item.getFirstChild();
                    String content=city.getNodeValue();

                    //arraylist 에 추가하기
                    cities.add(content);
                }
                //나머지 정보도 weather에 담기
//                for (int i =0; i<items2.getLength();i++){
//                    Weather weather = new Weather();
//                    Node node = items2.item(i);
//                    Node child = node.getFirstChild();
//                    String content = child.getNodeValue();
//                    weather.setTmEf(content);
//                    //
//                    Node node2 = items2.item(i);
//                    Node child2 = node.getFirstChild();
//                    String content2 = child.getNodeValue();
//                    weather.setTmx(content2);
//
//                    Node node3 = items2.item(i);
//                    Node child3 = node.getFirstChild();
//                    String content3 = child.getNodeValue();
//                    weather.setTmn(content3);
//
//                    Node node4 = items2.item(i);
//                    Node child4 = node.getFirstChild();
//                    String content4 = child.getNodeValue();
//                    weather.setWf(content4);
//
//                    //arrayList에 weather 정보 추가
//                    weathers.add(weather);
//
//
//                }


                //핸들러에게 메세지 전송
                handler.sendEmptyMessage(0);//콜백함수 호출


            } catch (Exception e) {
                Log.e("error","에러:"+e.getMessage());
                handler.sendEmptyMessage(1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("기상청 xml파싱");
        //멤버 변수 초기화
        cities= new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,cities);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        //각종 속성 지정하기
        listView.setDivider(new ColorDrawable(Color.RED));
        listView.setDividerHeight(3);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //버튼 이벤트
        findViewById(R.id.btnXml).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=progressDialog.show(MainActivity.this,"날씨 데이타","기상청정보가져오는중");
                ThreadEx th = new ThreadEx();
                th.start();

            }
        });

    }
}
