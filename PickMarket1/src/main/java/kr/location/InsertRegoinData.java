package kr.location;

import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InsertRegoinData {
    public static void main(String[] args) {
        insert();  // insert 실행
    }

    // DB 연결 정보
    private static final String DB_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@211.238.142.200:1521:xe";
    private static final String DB_ID = "steam02";
    private static final String DB_PASSWORD = "sumse02";

    // DB 연결 메서드
    private static Connection getConnection() throws Exception {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_ID, DB_PASSWORD);
    }

    public static void insert() {
        try (
            Reader reader = new InputStreamReader(
                InsertRegoinData.class.getResourceAsStream("location.json")
            );
            Connection conn = getConnection()
        ) {
            if (reader == null) {
                System.out.println("❌ 리소스 파일을 찾을 수 없습니다: location.json");
                return;
            }

            Gson gson = new Gson();
            List<Map<String, String>> regionList = gson.fromJson(reader, new TypeToken<List<Map<String, String>>>() {}.getType());

            // 🔍 JSON 중복 체크용 Set
            Set<String> seenRegionCds = new HashSet<>();

            String sql = "INSERT INTO location (region_cd, sido_cd, sgg_cd, umd_cd, region_nm) VALUES (?, ?, ?, ?, ?)";
            String checkSql = "SELECT COUNT(*) FROM location WHERE region_cd = ?";

            try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                PreparedStatement checkPstmt = conn.prepareStatement(checkSql)
            ) {
                pstmt.clearBatch();  // 🔧 혹시 모를 배치 클리어

                for (Map<String, String> region : regionList) {
                    // 값들 공백 제거
                    String regionCd = region.get("region_cd").trim();
                    String sidoCd = region.get("sido_cd").trim();
                    String sggCd = region.get("sgg_cd").trim();
                    String umdCd = region.get("umd_cd").trim();
                    String regionNm = region.get("region_nm").trim();

                    // 🔍 JSON 내부 중복 region_cd 체크
                    if (!seenRegionCds.add(regionCd)) {
                        System.out.println("⚠️ JSON 파일 내부 중복: " + regionCd);
                        continue;
                    }

                    // 🔍 DB에 이미 존재하는 region_cd 체크
                    checkPstmt.setString(1, regionCd);
                    ResultSet rs = checkPstmt.executeQuery();
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        if (count > 0) {
                            System.out.println("🚫 이미 DB에 존재: " + regionCd);
                            continue;
                        }
                    }

                    // 삽입 준비
                    pstmt.setString(1, regionCd);
                    pstmt.setString(2, sidoCd);
                    pstmt.setString(3, sggCd);
                    pstmt.setString(4, umdCd);
                    pstmt.setString(5, regionNm);
                    pstmt.addBatch();  // 배치에 추가
                    System.out.println("✅ 삽입 준비 완료: " + regionCd);
                }

                pstmt.executeBatch();  // 배치 실행
                System.out.println("✅ 데이터 삽입 완료");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("❌ 데이터 삽입 중 오류 발생");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ DB 연결 실패 또는 JSON 파일 처리 실패");
        }
    }
}
