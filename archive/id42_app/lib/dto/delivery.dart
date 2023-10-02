
class Delivery {
   final int id;
   final String description;
   final String createTime;

  const Delivery({
    required this.id,
    required this.description,
    required this.createTime,
  });

  factory Delivery.fromJson(Map<String, dynamic> json) {
    return Delivery(
      id: json['id'],
      description: json['description'],
      createTime: json['createTime'],
    );
  }

  @override
  String toString() {
    return 'Delivery{id: $id, description: $description, createTime: $createTime}';
  }
}