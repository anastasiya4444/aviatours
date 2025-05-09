package com.bsuir.aviatours.service.business;

import com.bsuir.aviatours.dto.BookingDTO;
import com.bsuir.aviatours.service.implementations.BookingServiceImpl;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class BookingPdfService {
    private final BookingServiceImpl bookingService;

    public BookingPdfService(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    public ByteArrayOutputStream generateBookingReportPdf(Integer bookingId) {
        BookingDTO booking = BookingDTO.fromEntity(bookingService.findEntityById(bookingId));
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        return createPdfDocument(booking);
    }

    private ByteArrayOutputStream createPdfDocument(BookingDTO booking) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont("src/main/resources/fonts/arialmt.ttf", PdfEncodings.IDENTITY_H, true);

            // Заголовок
            document.add(new Paragraph("Отчёт о бронировании")
                    .setFont(font)
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            // Основная информация
            document.add(new Paragraph("\nДетали бронирования:")
                    .setFont(font)
                    .setBold());

            Table bookingTable = new Table(2);
            bookingTable.addCell("ID бронирования:").setFont(font);
            bookingTable.addCell(String.valueOf(booking.getId())).setFont(font);
            bookingTable.addCell("Дата создания:").setFont(font);
            bookingTable.addCell(booking.getCreatedAt().toString()).setFont(font);
            document.add(bookingTable);

            // Информация о маршруте
            if (booking.getRoute() != null) {
                document.add(new Paragraph("\nИнформация о маршруте:")
                        .setFont(font)
                        .setBold());

                Table routeTable = new Table(2);
                routeTable.addCell("Аэропорт вылета:").setFont(font);
                routeTable.addCell(booking.getRoute().getDepartureAirportCode()).setFont(font);
                routeTable.addCell("Аэропорт прилёта:").setFont(font);
                routeTable.addCell(booking.getRoute().getArrivalAirportCode()).setFont(font);
                routeTable.addCell("Дата вылета:").setFont(font);
                routeTable.addCell(booking.getRoute().getDepartureDate().toString()).setFont(font);
                routeTable.addCell("Дата прилёта:").setFont(font);
                routeTable.addCell(booking.getRoute().getArrivalDate().toString()).setFont(font);
                document.add(routeTable);
            }

            // Информация о платеже
            if (booking.getPayment() != null) {
                document.add(new Paragraph("\nПлатёжная информация:")
                        .setFont(font)
                        .setBold());

                Table paymentTable = new Table(2);
                paymentTable.addCell("Способ оплаты:").setFont(font);
                paymentTable.addCell(booking.getPayment().getPaymentMethod()).setFont(font);
                paymentTable.addCell("Сумма:").setFont(font);
                paymentTable.addCell(booking.getPayment().getAmount().toString()).setFont(font);
                document.add(paymentTable);
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
        return outputStream;
    }
}