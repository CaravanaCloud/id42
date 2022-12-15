import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:id42_app/dto/delivery.dart';
import 'package:id42_app/request_delivery.dart';
import 'package:latlong2/latlong.dart';
import 'dart:convert';
import 'dart:html';
import 'package:http/http.dart' as http;


class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  Widget getMap() {
    return FlutterMap(
      options: MapOptions(
        center: LatLng(41.3874, 2.1686),
        zoom: 12,
      ),
      nonRotatedChildren: [
        AttributionWidget.defaultWidget(
          source: "OpenStreetMap contributors",
          onSourceTapped: null,
        ),
      ],
      children: [
        TileLayer(
          urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
          userAgentPackageName: 'id42.cc',
        ),
      ],
    );
  }

  //TODO: Avoid hard-coding client id
  GoogleSignIn _googleSignIn = GoogleSignIn(
    clientId: '183177550623-csndf21r6gf7ttbfjpmv4o47dnvn819g.apps.googleusercontent.com',
    scopes: [
      'email'
    ],
  );

  void _navigateToDeliveryRequest() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => const RequestDelivery()),
    );
  }

  void noop()  {
    // _handleSignIn();
    // _navigateToDeliveryRequest();
    print("Fetching delivery");
    final futureDelivery = fetchDelivery();
    futureDelivery.then((delivery) => print(delivery.toString()));
  }


  Future<Delivery> fetchDelivery() async {
    final url = apiUrl('/api/deliveries/1');
    final response = await http
        .get(Uri.parse(url));
    final statusCode = response.statusCode;
    print(url + " = " + statusCode.toString());
    if (statusCode == 200) {
      // If the server did return a 200 OK response,
      // then parse the JSON.
      return Delivery.fromJson(jsonDecode(response.body));
    } else {
      // If the server did not return a 200 OK response,
      // then throw an exception.
      throw Exception('Failed to load album');
    }
  }

  Future<void> _handleSignIn() async {
    try {
      await _googleSignIn.signIn();
    } catch (error) {
      print(error);
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
            body: getMap(),
            floatingActionButton: FloatingActionButton(
              onPressed: () => noop(),
              tooltip: 'Request delivery',
              child: const Icon(Icons.add),
            )
        )
    );
  }

  static const API_KEY = String.fromEnvironment('API_URL');

  String apiUrl(String s) {
    return "http://localhost:8182" + s;
  }
}
