package com.remonsinnema.read2understand.inbound.http;

import com.remonsinnema.read2understand.application.services.Upload;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/")
@Controller
@RequiredArgsConstructor
@Slf4j
public class UploadController {

  private final Upload upload;

  @GetMapping
  public String showUploadForm() {
    return "upload";
  }

  @PostMapping
  public String upload(
      @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, Model model) {
    if (file.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "Please select a PDF to upload");
      return "upload";
    }
    if (file.getOriginalFilename() == null || !file.getOriginalFilename().endsWith(".pdf")) {
      redirectAttributes.addFlashAttribute("message", "Only PDF files are supported at this time");
      return "upload";
    }
    try {
      model.addAttribute("result", uploadPdf(file));
    } catch (Exception e) {
      log.error("Failed to upload PDF", e);
      model.addAttribute("error", rootCauseOf(e).getMessage());
    }
    return "upload";
  }

  private Throwable rootCauseOf(Throwable e) {
    var cause = e.getCause();
    if (cause == null || cause == e) {
      return e;
    }
    return rootCauseOf(cause);
  }

  @SuppressWarnings("DataFlowIssue")
  private String uploadPdf(MultipartFile file) throws IOException {
    var dir = Paths.get("uploads");
    if (!Files.exists(dir)) {
      Files.createDirectories(dir);
    }
    var path = dir.resolve(file.getOriginalFilename());
    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

    return upload.pdf(path.toFile());
  }
}
