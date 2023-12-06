package com.office.library.book.admin.util;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {

	public String upload(MultipartFile file) {				//MultipartFile 타입의 파일을 업로드합니다.
		System.out.println("[UploadFileService] upload()");
		
		boolean result = false;
		
		//파일저장
		String fileOriName = file.getOriginalFilename();	//관리자가 업로드한 원본 파일의 이름을 가져옵니다.
		String fileExtension =								//관리자가 업로드한 원본 파일의 (확장자)를 가져옵니다.
				fileOriName.substring(fileOriName.lastIndexOf("."),fileOriName.length());
		String uploadDir = "C:\\library\\upload\\";			//서버에서 파일이 저장되는 위치를 지정합니다(저장경로)
		
		UUID uuid = UUID.randomUUID();							//UUID클래스를 이용해서 유일한 식별자를 얻습니다
		String uniqueName = uuid.toString().replaceAll("-", "");//(파일 이름)의 중복을 막기위해 관리자가 업로드한 파일의 이름을 변경해서 서버에 저장합니다.
		
		File saveFile = new File(uploadDir+"\\"+uniqueName+fileExtension);//서버에 저장되는 파일객체를 생성합니다(저장경로+파일이름+확장자)
		
		if(!saveFile.exists())	//만약 서버에 파일이 저장되는 디렉터리가 없다면
			saveFile.mkdirs();	//새 디렉터리를 만듭니다
		
		try {
			file.transferTo(saveFile);	//서버에 파일을 저장하고
			result = true;				//result를 true로 변경합니다
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(result) {					//result가 true이면 파일 업로드 성공, 파일이름과 확장자를 결합해서 반환합니다
			System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!!()");
			return uniqueName + fileExtension;		
		}else {							//파일 업로드에 실패했다면 null을 반환합니다
			System.out.println("[UploadFileService] FILE UPLOAD FAIL!!()");
			return null;
		}
	}

}
