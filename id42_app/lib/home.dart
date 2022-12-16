import 'package:flutter/material.dart';
import 'dart:ui' as ui;

import 'package:id42_app/map.dart';

class HomeUI extends StatelessWidget {
  const HomeUI({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ID 42',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MapUI(title: 'Next-gen delivery app'),
    );
  }
}

