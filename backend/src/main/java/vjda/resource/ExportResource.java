package vjda.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ClassPathResource;

@Path("export")
public class ExportResource {

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response export() throws IOException {
		// Get a template xlsx file from class path.
		InputStream is = new ClassPathResource("template.xlsx").getURL().openStream();
		final Workbook wb = WorkbookFactory.create(is);
		wb.getSheetAt(0).getRow(1).getCell(2).setCellValue("abc");;
		wb.getSheetAt(0).getRow(2).getCell(2).setCellValue("123");;

		StreamingOutput fileStream =  new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				wb.write(output);
			}
		};
		return Response.ok(fileStream).header("Content-Disposition", "attachment; filename = out.xlsx").build();
	}
}
