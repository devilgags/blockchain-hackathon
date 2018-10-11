package com.mindtree.TreeFelling.servicesImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.mindtree.TreeFelling.dto.LandDto;
import com.mindtree.TreeFelling.dto.TreeDto;
import com.mindtree.TreeFelling.services.DocUtilService;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

@Service
public class DocUtilServiceImpl implements DocUtilService {

//	public static void main(String[] args) throws IOException {
//		Document document = new Document();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		try {
//
//			Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
//			Chunk chunk = new Chunk("This is the title", chapterFont);
//			Chapter chapter = new Chapter(new Paragraph(chunk), 1);
//			Chunk linebreak = new Chunk(new DottedLineSeparator());
//
//			PdfPTable table = new PdfPTable(2);
//			table.setWidthPercentage(60);
//			table.setWidths(new int[] { 2, 2 });
//
//			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//
//			PdfPCell hcell;
//			hcell = new PdfPCell(new Phrase("Key", headFont));
//			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.addCell(hcell);
//
//			hcell = new PdfPCell(new Phrase("Value", headFont));
//			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.addCell(hcell);
//
//			PdfPCell cell;
//
//			cell = new PdfPCell(new Phrase("User Id"));
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("gags"));
//			cell.setPaddingLeft(5);
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("Land Id"));
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("sdfasdfas"));
//			cell.setPaddingLeft(5);
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("District"));
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("sdfvasd"));
//			cell.setPaddingLeft(5);
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("Taluk"));
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("scvasdvasd"));
//			cell.setPaddingLeft(5);
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("GPS"));
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("vdfvdsfv"));
//			cell.setPaddingLeft(5);
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("total"));
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("dfsvdfv"));
//			cell.setPaddingLeft(5);
//			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			table.addCell(cell);
//
//			PdfWriter.getInstance(document, out);
//			document.open();
//			document.add(chapter);
//			document.add(linebreak);
//			document.add(table);
//
//			document.close();
//
//		} catch (DocumentException ex) {
//
//			Logger.getLogger(DocUtilServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//		}
//
//		FileOutputStream fos = new FileOutputStream("aaaccb.pdf");
//		fos.write(out.toByteArray());
//		fos.close();
//		// return new ByteArrayInputStream(out.toByteArray());
//
//	}

	@Override
	public String createLandDoc(LandDto dto) throws IOException {
		// TODO Auto-generated method stub
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(60);
			table.setWidths(new int[] { 2, 2 });
			Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
			Chunk chunk = new Chunk("Land Registration Document", chapterFont);
			Chapter chapter = new Chapter(new Paragraph(chunk), 1);
			Chunk linebreak = new Chunk(new DottedLineSeparator());
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Key", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Value", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			PdfPCell cell;

			cell = new PdfPCell(new Phrase("User Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getUserId()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Land Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getLandId()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("District"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getDistrict()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Taluk"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getTaluk()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("GPS"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getGPS()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Total Extent"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getTotalExtent()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Created On"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getCreatedOn()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(chapter);
			document.add(linebreak);
			document.add(table);

			document.close();

		} catch (DocumentException ex) {

			Logger.getLogger(DocUtilServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		String dest = dto.getUserId() + "-" + String.valueOf(new Date().getTime()) + ".pdf";
		FileOutputStream fos = new FileOutputStream(dest);
		fos.write(out.toByteArray());
		fos.close();
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(dest));
		MerkleNode addResult = ipfs.add(file).get(0);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + addResult.hashCode());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + addResult.hash.toBase58());

		return addResult.hash.toBase58();
	}
	// Multihash filePointer =
	// Multihash.fromBase58("QmbTeXfYQd2LpNUTrpXear18aEizKw32xzxKYEFxgVKWJ4");
	// byte[] fileContents = ipfs.cat(filePointer);
	// System.out.println(fileContents.toString());
	// try (FileOutputStream fos = new FileOutputStream("hello_gen.txt")) {
	// fos.write(fileContents);
	// } catch (IOException ioe) {
	// ioe.printStackTrace();
	// }
	// return new ByteArrayInputStream(out.toByteArray());
	// }

	@Override
	public byte[] getDocBytes(String hash) throws IOException {
		// TODO Auto-generated method stub

		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
		Multihash filePointer = Multihash.fromBase58("QmbTeXfYQd2LpNUTrpXear18aEizKw32xzxKYEFxgVKWJ4");
		byte[] fileContents = ipfs.cat(filePointer);
		System.out.println(">>>>>>>>>>>>>>> file contents : " + fileContents.toString());
		return fileContents;
	}

	@Override
	public String createSurveyDoc(TreeDto dto, String userId) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(60);
			table.setWidths(new int[] { 2, 2 });
			Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
			Chunk chunk = new Chunk("Survey Document", chapterFont);
			Chapter chapter = new Chapter(new Paragraph(chunk), 1);
			Chunk linebreak = new Chunk(new DottedLineSeparator());
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Key", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Value", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			PdfPCell cell;

			cell = new PdfPCell(new Phrase("User Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(userId));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Tree Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getTreeId()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Location"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getLocation()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Survey Number"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getSurveyNo()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Approx Tree Area"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getApproxTreeArea()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("YearOfPlantation"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getYearOfPlantation()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Land Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getLandId()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Created On"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getCreatedOn()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(chapter);
			document.add(linebreak);
			document.add(table);

			document.close();

		} catch (DocumentException ex) {

			Logger.getLogger(DocUtilServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		String dest = userId + "-" + String.valueOf(new Date().getTime()) + ".pdf";
		FileOutputStream fos = new FileOutputStream(dest);
		fos.write(out.toByteArray());
		fos.close();
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(dest));
		MerkleNode addResult = ipfs.add(file).get(0);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + addResult.hashCode());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + addResult.hash.toBase58());

		return addResult.hash.toBase58();
	}

	@Override
	public String createRevenueDoc(TreeDto dto, String userId) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(60);
			table.setWidths(new int[] { 2, 2 });
			Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
			Chunk chunk = new Chunk("Revenue Verifictaion Document", chapterFont);
			Chapter chapter = new Chapter(new Paragraph(chunk), 1);
			Chunk linebreak = new Chunk(new DottedLineSeparator());
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Key", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Value", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			PdfPCell cell;

			cell = new PdfPCell(new Phrase("User Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(userId));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Tree Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getTreeId()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Location"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getLocation()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Survey Number"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getSurveyNo()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Approx Tree Area"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getApproxTreeArea()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("YearOfPlantation"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getYearOfPlantation()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Land Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getLandId()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Created On"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getCreatedOn()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Revenue Id"));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(dto.getRevenueId()));
			cell.setPaddingLeft(5);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(chapter);
			document.add(linebreak);
			document.add(table);

			document.close();

		} catch (DocumentException ex) {

			Logger.getLogger(DocUtilServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		String dest = userId + "-" + String.valueOf(new Date().getTime()) + ".pdf";
		FileOutputStream fos = new FileOutputStream(dest);
		fos.write(out.toByteArray());
		fos.close();
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(dest));
		MerkleNode addResult = ipfs.add(file).get(0);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + addResult.hashCode());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + addResult.hash.toBase58());

		return addResult.hash.toBase58();
	}

}
