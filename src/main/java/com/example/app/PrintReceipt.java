package com.example.app;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class PrintReceipt {
	public static void printReceipt(Order order) {
		try {
			String dest = "src/main/resources/Receipts/Receipt" + order.getOrderId() + ".pdf";
			PdfWriter writer = new PdfWriter(dest);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			document.add(new Paragraph("Order ID: " + order.getOrderId()));
			document.add(new Paragraph("Order Date: " + order.getDateOrdered()));
			document.add(new Paragraph("Customer: " +  Customer.getFullName(order.getCustomerId())));
			if (order.getOrderType().equals("Delivery")) {
				document.add(new Paragraph(Address.getAddresstxt(order.getAddressId())));
			}
			document.add(new Paragraph("Served By: " + HomeScreen.getUserLoggedIn()));
			System.out.println("-------------------------------------------");
			for (String orderItemid : order.getOrderItems()) {
				OrderItem orderItem = OrderItem.getOrderItemFromdb(orderItemid);
				document.add(new Paragraph(orderItem.getName() + "\t"+ orderItem.getQuantity() + "\t" + orderItem.getTotalPrice() + " $"));
				for (String removedIngredient : orderItem.getRemovedIngredients()) {
					document.add(new Paragraph("   -: " + removedIngredient));
				}
			}
			System.out.println("-------------------------------------------");
			document.add(new Paragraph("SubTotal: " + order.getTotal()));
			document.add(new Paragraph("Tax: " + order.getTax()));
			document.add(new Paragraph("Total: " + (((order.getTotal()) * (order.getTax()/100))+(order.getTotal()))));
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
