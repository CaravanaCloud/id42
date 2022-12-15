import 'package:flutter/material.dart';
import 'dart:ui' as ui;

import 'package:id42_app/home.dart';

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ID 42',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Next-gen delivery app'),
    );
  }
}

