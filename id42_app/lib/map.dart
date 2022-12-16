import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:id42_app/dto/delivery.dart';
import 'package:id42_app/request_delivery.dart';
import 'package:latlong2/latlong.dart';
import 'dart:convert';
import 'dart:html';
import 'package:http/http.dart' as http;


class MapUI extends StatefulWidget {
  const MapUI({super.key, required this.title});

  final String title;

  @override
  State<MapUI> createState() => _MapUIState();
}

class _MapUIState extends State<MapUI> {
  bool anonymous = true;

  void login() {
    anonymous = false;
  }

  void logout() {
    anonymous = true;
  }

  Widget getMap() {
    return FlutterMap(
      options: MapOptions(
        center: LatLng(41.3874, 2.1686),
        zoom: 12,
      ),
      nonRotatedChildren: [
        AttributionWidget.defaultWidget(
          source: "OpenStreetMap",
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
    if (anonymous) {
     _handleSignIn();
     anonymous = false;
    } else {
      printDelivery();
    }
     // _navigateToDeliveryRequest();
  }

    void printDelivery(){
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
              tooltip: 'Request delivery 20221612-121752',
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
