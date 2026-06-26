# InsightDesk – Hafta 1 Dağıtım Notları

## Amaç

Bu çalışmada, kendi bilgisayarımda geliştirdiğim InsightDesk uygulamasını AWS üzerinde çalışan bir EC2 Linux sunucusuna manuel olarak taşıdım. Uygulama, kullanıcıların geri bildirim gönderebildiği bir web formundan oluşmaktadır. Gönderilen bilgiler PostgreSQL veritabanına `received` durumu ile kaydedilmektedir.

Çalışmanın sonunda uygulama internete açık bir EC2 sunucusunda çalışır hale geldi ve tarayıcı üzerinden public IP adresi ile erişilebilir oldu.

---

## 1. Yerel geliştirme ortamının hazırlanması

İlk olarak Mac bilgisayarımda `insightdesk` adlı proje klasörünü oluşturdum.

Spring Initializr kullanarak Java ve Spring Boot tabanlı bir proje oluşturdum. Projede aşağıdaki temel teknolojiler kullanıldı:

* Java
* Spring Boot
* Spring Web
* Thymeleaf
* Spring Data JPA
* PostgreSQL Driver
* Validation
* Maven

Yerel veritabanı için Homebrew kuruldu ve PostgreSQL 18 yüklendi.

Ardından PostgreSQL servisi başlatıldı ve proje için ayrı bir veritabanı oluşturuldu:

```sql
CREATE DATABASE insightdesk;
```

Bu veritabanı, yerelde kullanıcıların gönderdiği geri bildirimleri saklamak için kullanıldı.

---

## 2. Spring Boot uygulamasının PostgreSQL’e bağlanması

`application.properties` dosyasında Spring Boot uygulamasının yerel PostgreSQL veritabanına bağlanması için gerekli bilgiler tanımlandı.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/insightdesk
spring.datasource.username=...
spring.datasource.password=...
```

Bu ayarlar sayesinde uygulama, veritabanının kendi bilgisayarımda çalıştığını ve PostgreSQL’in 5432 portunu kullandığını öğrendi.

Uygulama daha sonra aşağıdaki komutla çalıştırıldı:

```bash
./mvnw spring-boot:run
```

Terminalde `Tomcat started on port 8080` ve `Started InsightdeskApplication` mesajları görüldü. Bu, Spring Boot uygulamasının başarıyla çalıştığını gösterdi.

---

## 3. Web sayfası ve geri bildirim formunun oluşturulması

Uygulamanın ana sayfası için `index.html` dosyası oluşturuldu.

HTML ile aşağıdaki alanları içeren bir geri bildirim formu tasarlandı:

* Title
* Description
* Contact Email
* Send Feedback butonu

CSS kullanılarak formun görünümü düzenlendi. Sayfa ortalanmış bir kart yapısına dönüştürüldü; giriş alanları, buton ve renkler düzenlendi.

HTML, sayfada hangi bileşenlerin bulunacağını belirledi. CSS ise bu bileşenlerin nasıl görüneceğini belirledi.

---

## 4. Java tarafında geri bildirim yapısının oluşturulması

Kullanıcının formdan gönderdiği bilgilerin hangi alanlardan oluşacağını tanımlamak için `Feedback.java` dosyası oluşturuldu.

Bu sınıfta aşağıdaki alanlar tanımlandı:

* `id`
* `title`
* `body`
* `contactEmail`
* `status`
* `createdAt`

`Feedback` sınıfı PostgreSQL’deki `feedback` tablosuna karşılık gelecek şekilde tanımlandı.

Her yeni geri bildirim kaydının başlangıç durumu otomatik olarak aşağıdaki şekilde ayarlandı:

```text
received
```

Bu sayede kullanıcı geri bildirim gönderdiğinde, kayıt daha sonra AI analizi yapılmak üzere sistemde bekleyen bir kayıt olarak saklanmaktadır.

---

## 5. Veritabanı kayıt işleminin eklenmesi

`FeedbackRepository.java` dosyası oluşturuldu.

Bu dosya, Spring Data JPA kullanarak `Feedback` kayıtlarının PostgreSQL’e kaydedilmesini sağladı.

Repository sayesinde SQL komutlarını elle yazmadan aşağıdaki işlemler yapılabilir hale geldi:

* Yeni geri bildirim kaydetme
* Kayıtları listeleme
* Belirli bir kaydı bulma
* Kayıt silme

Formdan gelen verileri karşılamak için `HomeController.java` dosyası güncellendi.

Controller iki temel görevi yerine getirdi:

```text
GET /
→ index.html sayfasını gösterir

POST /feedback
→ Form verisini alır ve PostgreSQL’e kaydeder
```

Form gönderildiğinde kullanıcıdan alınan `title`, `body` ve `contactEmail` bilgileri bir `Feedback` nesnesine dönüştürüldü.

Ardından şu işlem yapıldı:

```java
feedbackRepository.save(feedback);
```

Bu komut, geri bildirimi PostgreSQL’deki `feedback` tablosuna kaydetti.

Yerel test sırasında PostgreSQL’de aşağıdaki sorgu çalıştırıldı:

```sql
SELECT * FROM feedback;
```

Formdan gönderilen kaydın veritabanında göründüğü ve `status` alanının `received` olduğu doğrulandı.

---

## 6. AWS hesabının hazırlanması

AWS hesabında maliyet kontrolü için aylık bütçe oluşturuldu.

Bütçe için e-posta bildirimleri tanımlandı. Böylece belirlenen harcama seviyelerine ulaşıldığında e-posta ile uyarı alınması sağlandı.

Ayrıca günlük AWS kullanımı için IAM kullanıcısı oluşturuldu ve MFA etkinleştirildi.

Bu aşamada amaç, root hesabı ile sürekli çalışmak yerine daha güvenli bir kullanıcı hesabı kullanmaktı.

---

## 7. EC2 Linux sunucusunun oluşturulması

AWS üzerinde yeni bir EC2 instance oluşturuldu.

Kullanılan temel ayarlar:

* Operating System: Amazon Linux 2023
* Instance Type: t3.micro
* Storage: 8 GiB gp3
* Architecture: 64-bit x86
* Security Group: Yeni oluşturulan güvenlik grubu

SSH bağlantısı için `.pem` anahtar dosyası oluşturuldu ve bilgisayara indirildi.

Anahtar dosyasının izinleri aşağıdaki komut ile sınırlandırıldı:

```bash
chmod 400 insightdesk-key.pem
```

Bu işlem, private key dosyasının başka kullanıcılar tarafından okunmasını engellemek için yapıldı.

---

## 8. EC2’ye SSH ile bağlanılması

Mac bilgisayarından EC2 sunucusuna aşağıdaki benzer komutla bağlanıldı:

```bash
ssh -i insightdesk-key.pem ec2-user@PUBLIC_IP_ADDRESS
```

Amazon Linux için kullanılan kullanıcı adı:

```text
ec2-user
```

SSH bağlantısı başarılı olduğunda artık Mac bilgisayarı yerine AWS veri merkezindeki Linux sunucusunda komut çalıştırılmaya başlandı.

Sunucu üzerinde temel Linux kontrolleri yapıldı:

```bash
whoami
pwd
ls -la
uname -a
df -h
free -h
```

Bu komutlarla kullanıcı bilgisi, klasör yapısı, Linux sürümü, disk alanı ve bellek durumu incelendi.

---

## 9. EC2 üzerinde Java ve PostgreSQL kurulumu

EC2 sunucusunda önce işletim sistemi paketleri güncellendi:

```bash
sudo dnf update -y
```

Ardından Java 21 Amazon Corretto kuruldu:

```bash
sudo dnf install -y java-21-amazon-corretto-devel
```

Kurulum aşağıdaki komutla doğrulandı:

```bash
java -version
```

Daha sonra PostgreSQL 16 server paketi kuruldu:

```bash
sudo dnf install -y postgresql16-server
```

PostgreSQL veri klasörü başlatıldı:

```bash
sudo postgresql-setup --initdb
```

Servis başlatıldı ve sunucu yeniden başladığında otomatik açılması sağlandı:

```bash
sudo systemctl enable --now postgresql
```

Servisin çalıştığı şu komutla kontrol edildi:

```bash
sudo systemctl status postgresql
```

---

## 10. EC2 üzerinde veritabanı ve uygulama kullanıcısının oluşturulması

PostgreSQL içinde InsightDesk için ayrı bir veritabanı oluşturuldu:

```sql
CREATE DATABASE insightdesk;
```

Uygulamanın PostgreSQL yönetici hesabını kullanmaması için ayrı bir uygulama kullanıcısı oluşturuldu.

Bu kullanıcıya `insightdesk` veritabanında gerekli yetkiler verildi.

PostgreSQL’in localhost üzerinden şifreli kullanıcı bağlantısını kabul etmesi için `pg_hba.conf` dosyasında ayarlamalar yapıldı.

Yerel bağlantılar için `ident` yöntemi yerine `scram-sha-256` yöntemi kullanıldı.

Bu değişiklikten sonra PostgreSQL yeniden başlatıldı:

```bash
sudo systemctl restart postgresql
```

Uygulama kullanıcısının veritabanına bağlanabildiği aşağıdaki komut ile doğrulandı:

```bash
psql -h localhost -U insightdesk_user -d insightdesk
```

---

## 11. Proje kodunun EC2’ye taşınması

Yerel bilgisayardaki proje dosyaları zip formatında EC2 sunucusuna kopyalandı.

Dosya aktarımı için `scp` komutu kullanıldı:

```bash
scp -i insightdesk-key.pem insightdesk.zip ec2-user@PUBLIC_IP_ADDRESS:/home/ec2-user/
```

EC2 içinde zip dosyası açıldı:

```bash
unzip insightdesk.zip
```

Proje klasörüne girildi:

```bash
cd ~/insightdesk
```

Projede aşağıdaki dosyaların bulunduğu doğrulandı:

```text
src/
pom.xml
mvnw
.mvn/
```

---

## 12. EC2 için uygulama ayarlarının düzenlenmesi

Yerel bilgisayardaki PostgreSQL kullanıcı bilgileri EC2’de geçerli olmadığı için EC2 içindeki `application.properties` dosyası güncellendi.

EC2 üzerinde Spring Boot uygulaması, PostgreSQL’e `localhost:5432` üzerinden bağlanacak şekilde ayarlandı.

Uygulama kullanıcı adı ve şifresi EC2’de oluşturulan PostgreSQL kullanıcısına göre ayarlandı.

Bu aşamada önemli bir problem yaşandı: `application.properties` dosyasına ayarlar yanlışlıkla birden fazla kez yazıldı ve bazı satırlar birleşti.

Örneğin aşağıdaki türde hatalı bir değer oluştu:

```text
spring.jpa.show-sql=truespring.application.name=insightdesk
```

Bu hata Spring Boot’un ayar dosyasını okuyamamasına neden oldu.

Dosya daha sonra tamamen temizlenerek yalnızca gerekli ayarlar bırakıldı.

Bu deneyim, manuel yapılandırmanın küçük yazım hatalarına karşı ne kadar hassas olduğunu gösterdi.

---

## 13. Uygulamanın EC2 üzerinde paketlenmesi

Maven Wrapper çalıştırılabilir hale getirildi:

```bash
chmod +x mvnw
```

Proje aşağıdaki komut ile paketlendi:

```bash
./mvnw clean package
```

İlk paketleme denemelerinde PostgreSQL bağlantı ve yapılandırma hataları alındı.

PostgreSQL kullanıcı doğrulaması ve `application.properties` dosyası düzeltildikten sonra paketleme başarılı oldu.

Terminalde aşağıdaki mesaj görüldü:

```text
BUILD SUCCESS
```

Bu işlem sonunda Spring Boot projesi çalıştırılabilir `.jar` dosyasına dönüştürüldü.

---

## 14. Uygulamanın EC2 üzerinde çalıştırılması

Paketlenen uygulama aşağıdaki komutla çalıştırıldı:

```bash
java -jar target/insightdesk-0.0.1-SNAPSHOT.jar
```

Uygulama başladığında aşağıdaki mesajlar görüldü:

```text
Tomcat started on port 8080
Started InsightdeskApplication
```

Bu, Spring Boot uygulamasının EC2 üzerinde çalıştığını ve PostgreSQL veritabanına bağlandığını gösterdi.

---

## 15. Security Group ayarları ve dış erişim

Başlangıçta EC2 güvenlik grubunda yalnızca SSH bağlantısı için 22 numaralı port açıktı.

SSH erişimi yalnızca kendi IP adresimden gelecek şekilde sınırlandırıldı.

Uygulamanın dışarıdan erişilebilir olması için güvenlik grubuna yeni bir inbound rule eklendi:

```text
Type: Custom TCP
Port: 8080
Source: Anywhere-IPv4
```

Bu ayar sayesinde kullanıcılar tarayıcıdan EC2 public IP adresine `8080` portu üzerinden erişebildi.

PostgreSQL’in kullandığı `5432` portu kesinlikle dışarıya açılmadı.

Bu önemliydi çünkü kullanıcıların doğrudan veritabanına erişmesini istemiyoruz. Kullanıcılar sadece web uygulamasına erişebilmeli; veritabanına yalnızca EC2 içindeki uygulama bağlanabilmelidir.

---

## 16. Canlı uç noktanın test edilmesi

Tarayıcı üzerinden aşağıdaki benzer adres açıldı:

```text
http://PUBLIC_IP_ADDRESS:8080
```

Başlangıçta Whitelabel Error Page görüldü. Bunun nedeni EC2’ye ilk gönderilen zip dosyasının eski sürüm olmasıydı.

Güncel `HomeController`, `Feedback`, `FeedbackRepository` ve `index.html` dosyaları EC2’ye tekrar gönderildi.

Uygulama yeniden paketlenip çalıştırıldıktan sonra InsightDesk geri bildirim formu dışarıdan erişilebilir hale geldi.

Dışarıdan form doldurularak geri bildirim gönderildi.

Son olarak EC2 içindeki PostgreSQL veritabanında aşağıdaki sorgu çalıştırıldı:

```sql
SELECT id, title, body, contact_email, status, created_at
FROM feedback
ORDER BY id DESC;
```

Tarayıcıdan gönderilen geri bildirimin EC2 üzerindeki PostgreSQL veritabanına kaydedildiği doğrulandı.

Kayıt için `status` değerinin `received` olduğu görüldü.

---

## Sonuç

Bu çalışma sonunda, yerel bilgisayarda geliştirilen Spring Boot uygulaması AWS EC2 üzerinde çalışan bir Linux sunucusuna manuel olarak taşındı.

Uygulama artık sadece kendi bilgisayarımda çalışan bir proje değildir. İnternete bağlı bir EC2 sunucusunda çalışmakta ve kullanıcılar tarafından public IP adresi üzerinden erişilebilmektedir.

Son sistem akışı aşağıdaki gibidir:

```text
Kullanıcı
   ↓
Tarayıcı
   ↓
EC2 Public IP:8080
   ↓
Spring Boot / InsightDesk Uygulaması
   ↓
PostgreSQL
   ↓
feedback tablosu
   ↓
status = received
```

Bu aşamada Docker, Kubernetes, Redis ve AI analiz servisi henüz kullanılmamıştır. Bu bileşenler sonraki haftalarda eklenecektir.
