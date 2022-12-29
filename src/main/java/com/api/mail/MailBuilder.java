package com.api.mail;


import com.exception.MailException;
import com.exception.enums.GlobalExceptionType;
import com.util.Encryption.EncryptionService;
import com.util.TimeFormatter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

@Service
@Slf4j
public class MailBuilder {
    @Autowired
    private Mail mail;
    private MimeMessage message;

    public MailBuilder() {
    }

    public MailBuilder setSession() throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", this.mail.getHost_address());
        props.put("mail.smtp.port", this.mail.getHost_port());
        if (mail.getHost_address().contains("google")) {
            props.put("mail.smtp.starttls.enable", this.mail.getHost_starttls());
        }
        props.put("mail.smtp.auth", this.mail.getHost_auth());

        message = new MimeMessage(Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @SneakyThrows
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail.getHost_email(), new EncryptionService().decryptAES(mail.getHost_password()));
            }
        }));
        message.setFrom(new InternetAddress(this.mail.getHost_email())); /*보내는 사람 메일 주소 API상으론 이메일로 로그인을 한 사람이 보낸다.*/
        return this;
    }

    public MailBuilder setTo(String received_address) throws MessagingException {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(received_address)); /*받는사람 메일 주소 API상으론 서버에 설정된 이메일로 받는다.*/
        return this;
    }

    public MailBuilder setMailTitle(String title) throws MessagingException {
        message.setSubject(title, "UTF-8");
        return this;
    }

    public MailBuilder setMailContent(String content, MailType type) throws MessagingException {
        /**
         * message.setText("보낼 메세지 내용");
         * message.setContent("보낼 HTML 내용", "text/html;charset=UTF-8");
         * */
        if (type == MailType.PASSWORD) {
            message.setContent(content, "text/html;charset=UTF-8");
        } else if (type == MailType.REGISTER) {
            message.setContent(content, "text/html;charset=UTF-8");
        } else if (type == MailType.WITHDRAWAL) {
            message.setContent(content, "text/html;charset=UTF-8");
        } else {
            throw new MailException(GlobalExceptionType.MAIL_EXCEPTION);
        }
        return this;
    }

    public void send() {
        try {
            Transport.send(message);
        } catch (SendFailedException e) {
            throw new MailException(GlobalExceptionType.MAIL_EXCEPTION);
        } catch (MessagingException e) {
            throw new MailException(GlobalExceptionType.MAIL_EXCEPTION);
        }
    }


    /**
     * @param : MailType(Enum), MailLogo(Object), MailFooter(Object), MailUser(Object, Optional), code(String, optional)
     * @return : String (String)
     * Description : 전송할 메일 컨텐츠를 만드는 함수
     * MailType 에 맞춰 보내면 되며
     * MailUser를 쓸 땐 무조건 MailType.REGISTER
     * code를 쓸 땐 MailType.WITHDRAWAL or MailType.PASSWORD를 사용
     * Date : 2022-07-27
     */
    public String getMailHTML(MailType type, MailLogo mailLogo, MailFooter mailFooter, MailUser mailUser) {
        if (!Objects.equals(type, MailType.REGISTER)) {
            // paramter 에 MailUser를 넣을 경우
            throw new MailException(GlobalExceptionType.MAIL_EXCEPTION);
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<table align=\"center\"\n" +
                "       width=\"100%\"\n" +
                "       cellspacing=\"0\"\n" +
                "       cellpadding=\"0\"\n" +
                "       border=\"0\"\n" +
                "       bgcolor=\"#ffffff\"\n" +
                "       style=\"max-width:600px;margin:0 auto\">\n" +
                "\n" +
                "  <tbody>\n" +
                "  <tr>\n" +
                "    <td height=\"45\"\n" +
                "        colspan=\"3 \"\n" +
                "        bgcolor=\"#FFF\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"\n" +
                "        height=\"25\"\n" +
                "        bgcolor=\"#FFF\"></td>\n" +
                "    <td height=\"25\"\n" +
                "        colspan=\"2\"\n" +
                "        bgcolor=\"#FFF\">\n" +
                // LOGO IMAGE DIV
                makeImageLogo(mailLogo) +
                "    </td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"45\"\n" +
                "        colspan=\"3\"\n" +
                "        bgcolor=\"#FFF\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"40\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                // CONTENT
                makeFooterHTML(mailFooter) +
                makeRegisterHtml(mailUser) +
                "  <!--[if (gte mso 9)|(IE)]>\n" +
                "\n" +
                "\n" +
                "  </table></td></tr></tbody>\n" +
                "  <![endif]-->\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
    }

    public String getMailHTML(MailType type, MailLogo mailLogo, MailFooter mailFooter, String code) {
        String content = Objects.equals(type, MailType.PASSWORD) ? makePasswordHTML(code) : makeWithDrawalHTML(code);
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<table align=\"center\"\n" +
                "       width=\"100%\"\n" +
                "       cellspacing=\"0\"\n" +
                "       cellpadding=\"0\"\n" +
                "       border=\"0\"\n" +
                "       bgcolor=\"#ffffff\"\n" +
                "       style=\"max-width:600px;margin:0 auto\">\n" +
                "\n" +
                "  <tbody>\n" +
                "  <tr>\n" +
                "    <td height=\"45\"\n" +
                "        colspan=\"3 \"\n" +
                "        bgcolor=\"#FFF\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"\n" +
                "        height=\"25\"\n" +
                "        bgcolor=\"#FFF\"></td>\n" +
                "    <td height=\"25\"\n" +
                "        colspan=\"2\"\n" +
                "        bgcolor=\"#FFF\">\n" +
                // LOGO IMAGE DIV
                makeImageLogo(mailLogo) +
                "    </td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"45\"\n" +
                "        colspan=\"3\"\n" +
                "        bgcolor=\"#FFF\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"40\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                // CONTENT
                makeFooterHTML(mailFooter) +
                content +
                "  <!--[if (gte mso 9)|(IE)]>\n" +
                "\n" +
                "\n" +
                "  </table></td></tr></tbody>\n" +
                "  <![endif]-->\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
    }


    /**
     * @param : MailLogo(Object)
     * @return : String (String)
     * Description : 메일 컨텐츠 내에 이미지 로고를 만드는 함수 (이미지 URL, alt)
     * Date : 2022-07-27
     */
    private String makeImageLogo(MailLogo mailLogo) {
        return "      <img src=\"" + mailLogo.getImageUrl() + "\"\n" +
                "           width=\"133\"\n" +
                "           height=\"41\"\n" +
                "           alt=\"" + mailLogo.getAlt() + "\"\n" +
                "           border=\"0\"\n" +
                "           style=\"display:block\"\n" +
                "           loading=\"lazy\">\n";
    }

    /**
     * @param : MailFooter(Object)
     * @return : String (String)
     * Description : 메일 컨텐츠 내에 Footer를 만드는 함수
     * (Footer 요소 :    회사명
     * 회사 영어명
     * 대표자명
     * 사업자 등록번호
     * 회사 주소 )
     * Date : 2022-07-27
     */
    private String makeFooterHTML(MailFooter mailFooter) {
        return "  <tr>\n" +
                "    <td height=\"40\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td height=\"1\"\n" +
                "        style=\"border-bottom-width:1px;border-bottom-color:#eee;border-bottom-style:solid;\"></td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"19\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td style=\"font-size:12px;line-height:18px;font-family:Apple SD Gothic Neo,sans-serif,'맑은고딕',Malgun Gothic,'돋움',Dotum;color:#7F7F7F\">\n" +
                "      본 메일은 발신전용입니다. 본 메일로 회신할 경우 답변이 되지 않습니다.<br>\n" +
                "      서비스 이용 및 법적 의무 고지사항 안내메일로써 수신동의 여부와 관계없이 발송드립니다.<br/>\n" +
                "      Copyrights ⓒ " + TimeFormatter.TimeFormatCurrent("yyyy") + " " + mailFooter.getCompanyEngName() + " INC. ALL RIGHTS RESERVED.\n" +
                "    </td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td>\n" +
                "      <table width=\"100%\"\n" +
                "             cellspacing=\"0\"\n" +
                "             cellpadding=\"0\"\n" +
                "             border=\"0\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "          <td height=\"12\"></td>\n" +
                "        </tr>\n" +
                "        <tr><!-- 2021-10-18 푸터 로고 이미지 변경 -->\n" +
                "          <td style=\"font-size:13px;line-height:20px;font-weight:bold;font-family:Apple SD Gothic Neo,sans-serif,'맑은고딕',Malgun Gothic,'돋움',Dotum;color:#4C4C4C\">\n" +
                "            " + mailFooter.getCompanyName() + "\n" + // COMPANY NAME
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"8\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td style=\"font-size:12px;line-height:18px;font-family:Apple SD Gothic Neo,sans-serif,'맑은고딕',Malgun Gothic,'돋움',Dotum;color:#7F7F7F\">\n" +
                "            대표\n" +
                "            <span style=\"display:inline-block;vertical-align:top;font-size: 10px;line-height: 17px;color: #B2B2B2;\">:</span>" + mailFooter.getRepresentativeName() + "\n" + // REPRESENTER NAME
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td style=\"font-size:13px;line-height:18px;font-family:Apple SD Gothic Neo,sans-serif,'맑은고딕',Malgun Gothic,'돋움',Dotum;color:#7F7F7F\">\n" +
                "            사업자등록번호\n" +
                "            <span style=\"display:inline-block;vertical-align:top;font-size: 10px;line-height: 17px;color: #B2B2B2;\">:</span>\n" +
                "            " + mailFooter.getBusinessNumber() + "\n" + // BUSINESS NUMBER
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td style=\"font-size:13px;line-height:18px;font-family:Apple SD Gothic Neo,sans-serif,'맑은고딕',Malgun Gothic,'돋움',Dotum;color:#7F7F7F\">\n" +
                "            주소\n" +
                "            <span style=\"display:inline-block;vertical-align:top;font-size: 10px;line-height: 17px;color: #B2B2B2;\">:</span>\n" +
                "            " + mailFooter.getCompanyAddress() + "\n" + // COMPANY ADDRESS
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"2\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"2\"></td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"3\"\n" +
                "        height=\"58\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"5\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n";
    }

    /**
     * @param : MailUser(Object)
     * @return : String (String)
     * Description : 메일 컨텐츠 내에 회원가입 html를 만드는 함수
     * (User 요소 :    이름
     * 아이디
     * 등록일자 )
     * Date : 2022-07-27
     */
    private String makeRegisterHtml(MailUser mailUser) {
        return "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td style=\"font-size:24px;line-height:32px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;font-weight:bold;color:#191919\">\n" +
                "      " + mailUser.getName() + "님 반갑습니다!\n" +
                "    </td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"11\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td style=\"font-size:14px;line-height:22px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;color:#7F7F7F\">\n" +
                "      회원가입이 정상적으로 완료되었습니다.\n" +
                "    </td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"14\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td>\n" +
                "      <table align=\"left\"\n" +
                "             width=\"100%\"\n" +
                "             cellspacing=\"0\"\n" +
                "             cellpadding=\"0\"\n" +
                "             border=\"0\"\n" +
                "             style=\"line-height:20px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;color:#191919;\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "          <td height=\"15\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"15\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"font-size:14px;letter-spacing:0;color:#4C4C4C\">\n" +
                "            회원가입 내역\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"7\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"1\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"border-bottom-width:1px;border-bottom-color:#eee;border-bottom-style:solid;\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"20\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td width=\"5\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              width=\"100\"\n" +
                "              style=\"font-size:14px;font-weight:normal\">아이디\n" +
                "          </td>\n" +
                "          <td width=\"10\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              style=\"font-size:14px;font-weight:bold;word-break:break-all\">" + mailUser.getId() + "\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"16\"\n" +
                "              colspan=\"6\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td width=\"5\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              width=\"100\"\n" +
                "              style=\"font-size:14px;font-weight:normal\">가입일\n" +
                "          </td>\n" +
                "          <td width=\"10\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              style=\"font-size:14px;font-weight:bold;word-break:break-all\">" + mailUser.getRegDate() + "\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"19\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"1\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"border-bottom-width:1px;border-bottom-color:#eee;border-bottom-style:solid;\"></td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "\n";
    }

    /**
     * @param : code(String)
     * @return : String (String)
     * Description : 메일 컨텐츠 내에 비밀번호 인증 html를 만드는 함수
     * Date : 2022-07-27
     */
    private String makePasswordHTML(String code) {
        return "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td style=\"font-size:24px;line-height:32px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;font-weight:bold;color:#191919\">\n" +
                "      비밀번호 찾기\n" +
                "    </td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"11\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td style=\"font-size:14px;line-height:22px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;color:#7F7F7F\">\n" +
                "      비밀번호 찾기를 위해 아래의 코드를 입력하여\n" +
                "      인증을 완료해 주세요.\n" +
                "    </td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"14\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td>\n" +
                "      <table align=\"left\"\n" +
                "             width=\"100%\"\n" +
                "             cellspacing=\"0\"\n" +
                "             cellpadding=\"0\"\n" +
                "             border=\"0\"\n" +
                "             style=\"line-height:20px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;color:#191919;\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "          <td height=\"15\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"15\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"font-size:14px;letter-spacing:0;color:#4C4C4C\">\n" +
                "            인증 내역\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"7\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"1\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"border-bottom-width:1px;border-bottom-color:#eee;border-bottom-style:solid;\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"20\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td width=\"5\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              width=\"100\"\n" +
                "              style=\"font-size:14px;font-weight:normal\">인증 타입\n" +
                "          </td>\n" +
                "          <td width=\"10\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              style=\"font-size:14px;font-weight:bold;word-break:break-all\">비밀번호\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"16\"\n" +
                "              colspan=\"6\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td width=\"5\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              width=\"100\"\n" +
                "              style=\"font-size:14px;font-weight:normal\">인증 번호\n" +
                "          </td>\n" +
                "          <td width=\"10\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              style=\"font-size:14px;font-weight:bold;word-break:break-all\">" + code + "\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"19\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"1\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"border-bottom-width:1px;border-bottom-color:#eee;border-bottom-style:solid;\"></td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td>\n" +
                "\n";
    }

    /**
     * @param : code(String)
     * @return : String (String)
     * Description : 메일 컨텐츠 내에 회원탈퇴 인증 html를 만드는 함수
     * Date : 2022-07-27
     */
    private String makeWithDrawalHTML(String code) {
        return "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td style=\"font-size:24px;line-height:32px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;font-weight:bold;color:#191919\">\n" +
                "      회원탈퇴 인증\n" +
                "    </td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"11\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td style=\"font-size:14px;line-height:22px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;color:#7F7F7F\">\n" +
                "      회원 탈퇴를 위해 아래의 코드를 입력하여\n" +
                "      인증을 완료해 주세요.\n" +
                "    </td>\n" +
                "    <td width=\"5\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"14\"\n" +
                "        colspan=\"3\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td>\n" +
                "      <table align=\"left\"\n" +
                "             width=\"100%\"\n" +
                "             cellspacing=\"0\"\n" +
                "             cellpadding=\"0\"\n" +
                "             border=\"0\"\n" +
                "             style=\"line-height:20px;font-family:'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;color:#191919;\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "          <td height=\"15\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"15\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"font-size:14px;letter-spacing:0;color:#4C4C4C\">\n" +
                "            인증 내역\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"7\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"1\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"border-bottom-width:1px;border-bottom-color:#eee;border-bottom-style:solid;\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"20\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td width=\"5\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              width=\"100\"\n" +
                "              style=\"font-size:14px;font-weight:normal\">인증 타입\n" +
                "          </td>\n" +
                "          <td width=\"10\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              style=\"font-size:14px;font-weight:bold;word-break:break-all\">회원탈퇴\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"16\"\n" +
                "              colspan=\"6\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td width=\"5\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              width=\"100\"\n" +
                "              style=\"font-size:14px;font-weight:normal\">인증 번호\n" +
                "          </td>\n" +
                "          <td width=\"10\"></td>\n" +
                "          <td valign=\"top\"\n" +
                "              style=\"font-size:14px;font-weight:bold;word-break:break-all\">" + code + "\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"19\"\n" +
                "              colspan=\"4\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td height=\"1\"\n" +
                "              colspan=\"4\"\n" +
                "              style=\"border-bottom-width:1px;border-bottom-color:#eee;border-bottom-style:solid;\"></td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"5\"></td>\n" +
                "    <td>\n" +
                "\n";
    }
}
