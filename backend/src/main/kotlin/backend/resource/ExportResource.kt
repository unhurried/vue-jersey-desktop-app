package backend.resource

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.StreamingOutput

@Path("export")
class ExportResource {
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Throws(IOException::class)
    fun export(): Response {
        // Get a template xlsx file from class path.
        val `is` = ClassPathResource("template.xlsx").url.openStream()
        val wb = WorkbookFactory.create(`is`)
        wb.getSheetAt(0).getRow(1).getCell(2).setCellValue("abc")
        wb.getSheetAt(0).getRow(2).getCell(2).setCellValue("123")
        val fileStream = StreamingOutput { output -> wb.write(output) }
        return Response.ok(fileStream).header("Content-Disposition", "attachment; filename = out.xlsx").build()
    }
}