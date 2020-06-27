import 'package:flutter/material.dart';

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:android_intent/android_intent.dart';
import 'package:http/http.dart' as http;



void main() => runApp(
    MaterialApp(
      home: KaKaoPayPage(),
      //routes: <String, WidgetBuilder>{
      //'/a' :(BuildContext context) => ReturnPage()
      //},
    )
);


class KaKaoPayPage extends StatefulWidget {
  @override
  _KaKaoPayPageState createState() => _KaKaoPayPageState();
}
/*
class ReturnPage extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: MaterialButton(
          child: Text('잔액충전 페이지로 돌아가기'),
          onPressed: ()async{
            AndroidIntent intent2 = AndroidIntent(
              action: 'android.intent.action.RUN',
              package:'com.example.a3rtubler1',
              componentName: 'com.example.a3rtubler1.BalanceCharge',
              //arguments: {'route' :
            );
            await intent2.launch();
          },
        ),
      ),
    );
  }
}
*/
class _KaKaoPayPageState extends State<KaKaoPayPage> {
  final _URL = 'http://10.210.24.35';
  final _ADMIN_KEY = '33e79d08e7a4cef970b41acace922aa0';


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(crossAxisAlignment: CrossAxisAlignment.center,
        children:[
          Expanded(
          child:MaterialButton(
          onPressed: () async{
            var res = await http.post(
                'https://kapi.kakao.com/v1/payment/ready',
                encoding: Encoding.getByName('utf8'),
                headers: {
                  'Authorization' : 'KakaoAK $_ADMIN_KEY'
                },
                body: {
                  'cid':'TC0ONETIME',
                  'partner_order_id':'partner_order_id',
                  'partner_user_id':'partner_user_id',
                  'item_name' : '3R 텀블러 잔액충전',
                  'quantity': '1',
                  'total_amount':'10000',
                  'vat_amount' : '100',
                  'tax_free_amount' : '0',
                  'approval_url' : '$_URL/kakaopayment',
                  'fail_url' : '$_URL/kakaopayment',
                  'cancel_url': '$_URL/kakaopayment'
                }
            );
            Map<String, dynamic> result = json.decode(res.body);
            AndroidIntent intent = AndroidIntent(
              action: 'action_view',
              data: result['next_redirect_mobile_url'],
              arguments: {'txn_id': result['tid'] },
            );
            await intent.launch();

            //결제승인
            /*
            var res1 = await http.post(
                'https://kapi.kakao.com/v1/payment/approve',
                encoding: Encoding.getByName('utf8'),
                headers: {
                  'Authorization' : 'KakaoAK $_ADMIN_KEY'
                },
                body: {
                  'cid':'TC0ONETIME',
                  'tid':result['tid'].toString(),
                  'partner_order_id':'partner_order_id',
                  'partner_user_id':'partner_user_id',
                  'pg_token' :result['PG_token'].toString(),
                  'total_amount':'10000'

                   }
            );
            Map<String, dynamic> result2 = json.decode(res.body);
            AndroidIntent intent4 = AndroidIntent(
              action: 'action_view',
              data: result['total_amount'],
              arguments: {'txn_id': result['tid']},
            );
            await intent4.launch();
*/
            },
          child: Text("3R텀블러 카카오페이로 잔액충전하기(클릭)",
            style: new TextStyle(
              fontSize: 20.0,
              color: Colors.green,
            ),),

        ),
    ),
          Expanded(
            child:MaterialButton(
              onPressed: ()async{
              AndroidIntent intent3 = AndroidIntent(
               action: 'android.intent.action.RUN',
                package:'com.example.a3rtubler1',
                componentName: 'com.example.a3rtubler1.BalanceCharge',
                //arguments: {'route' :
              );
              await intent3.launch();
              },
              child: Text("잔액충전 페이지로 돌아가기",
              style: new TextStyle(
                fontSize: 20.0,
                color: Colors.greenAccent,
              ),),
            )
          )
    ]
      ),
    );
  }
}