# InsightDesk – Kavram Sözlüğü

# Bu dosya, proje boyunca kullanılan bulut, yazılım ve DevOps kavramlarını kendi cümlelerimle açıklamak için hazırlanmıştır.

# Her kavram için şu üç soruya cevap vereceğim:

a. Nedir?
b. Hangi problemi çözer?
c. Bu projede nerede kullanılacak?


# 1. Bulut Bilişim (Cloud Computing)
Nedir?
Kendi bilgisayarında veya şirketindeki fiziksel sunuculardan işlem yapmak yerine, internet üzerinden 
başka şirketlerin(genel olarak büyük şirketlerin) bilgisayarlarını ve depolama hizmetlerini 
kullanmaktır.

IaaS — Infrastructure as a Service:
AWS bilgisayarı verir ve sen bilgisayarın içinde sistemi kurarsın.
Linux kurulu gelir ama ayarlarını sen yaparsın.
Uygulamanı sen kurarsın.
PostgreSQL’i sen kurarsın.
Portları ve güvenliği sen ayarlarsın.
bu projede en çok kullanacağımız model budur.

PaaS — Platform as a Service:
PaaS, sadece bilgisayar vermek yerine, uygulamanı çalıştırabileceğin hazır ortamı verir.
Linux kurulumu
Sunucu güncellemesi
Çoğu altyapı ayarı
Uygulamanın hangi makinede çalışacağı

Sen kodu verirsin platform onu çalıştırır.
bu projede bunu kullanmıyoruz

SaaS — Software as a Service:
SaaS, internetten kullandığın hazır uygulamadır.
Gmail
Google Drive
Notion
Canva
Zoom


Hangi problemi çözer?
Normalde bir web sitesi veya uygulama yayınlamak için:

fiziksel bilgisayar satın almam,
sürekli açık tutmam,
internet bağlantısını sağlamam,
elektrik, bakım ve güvenlikle uğraşmam gerekirdi.

Bulut bunu kolaylaştırır:
İhtiyacımız kadar sunucu kullanıp o kadar ödeme yapacağız.

yani mesela amazon bize uzaktaki hep açık bilgisayarı verir. ama yine kurulumu falan bizde.


Bu projede nerede kullanılır?
Önce macimde kodu yazarım. sonra bunu aws sunucuya koyarım.
Bu projede bulutu özellikle şu yerlerde kullanacağız:

AWS EC2: İnternette çalışan Linux sunucuyu açacağız. İlk hafta uygulamayı buraya elle kuracağız. 
Sonra aynı sunucuda Kubernetes kuracağız.
AWS IAM: AWS hesabına kim girebilir, EC2’nin Bedrock’a erişim izni var mı gibi yetkileri bununla 
yöneteceğiz.
Amazon Bedrock: Kullanıcının yazdığı geri bildirimi AI ile analiz edeceğiz. Örneğin “Bu uygulama 
sürekli hata veriyor” mesajını negative, bug, high urgency diye sınıflandıracak.
Muhtemelen ECR veya Docker Hub: Docker imajlarımızı saklamak için bir registry kullanacağız.
AWS ağ yapısı: EC2’nin internete çıkması, dışarıdan web sayfasına ulaşılması ve sadece gerekli 
portların açık olması için.

yani aws bize uygulamamızın çalışabileceği bir ortam sunacak.

# 2. Sanal makine
Nedir?
Sanal makine, bir bulut sağlayıcısının veri merkezindeki büyük fiziksel bir bilgisayarın içinde 
oluşturulan, ayrı bir bilgisayar gibi çalışan ortamdır. Amazon EC2, AWS üzerinde bu sanal 
bilgisayarları oluşturup kullanmamızı sağlayan hizmettir. Bir EC2 örneğinin kendine ait işlemcisi, 
RAM’i, depolama alanı, Linux işletim sistemi ve internet bağlantısı olabilir.

Hangi problemi çözer?
Kendi fiziksel sunucumuzu satın alma, sürekli açık tutma, bakımını yapma ve barındırma ihtiyacını 
ortadan kaldırır. İhtiyacımız olduğunda birkaç dakika içinde sunucu oluşturabilir, gücünü seçebilir, 
uzaktan bağlanabilir ve işimiz bittiğinde durdurabilir veya silebiliriz. İnsanlar projemize link ile 
bağlanabilir.

Bu projede nerede kullanılır?
Bu projede ilk hafta EC2, InsightDesk uygulamasını manuel olarak kuracağımız uzak Linux sunucusu 
olarak kullanılacaktır. Kendi bilgisayarımızda yazdığımız uygulamayı EC2’ye taşıyacak, gerekli y
azılımları kuracak, uygulamayı çalıştıracak ve internetten erişilebilir hale getireceğiz. Daha sonra 
daha güçlü bir EC2 üzerinde Kubernetes kurarak projenin tüm servislerini çalıştıracağız.


# 3. Linux ve Komut Satırı

Nedir?
Linux, birçok sunucuda kullanılan bir işletim sistemidir. Windows veya macOS gibi bilgisayarı 
çalıştırır; ancak sunucularda daha hafif, güvenli ve yönetilebilir olduğu için çok yaygındır. Komut 
satırı ise ekrandaki butonlara tıklamak yerine yazılı komutlarla bilgisayarı yönetme yöntemidir. 
Örneğin dosya oluşturmak, klasör değiştirmek, program kurmak veya çalışan bir uygulamayı kontrol 
etmek komutlarla yapılabilir.

Hangi problemi çözer?

Bir sunucu genellikle ekranı, klavyesi veya grafik arayüzü olmadan çalışır. Bu nedenle sunucuyu 
uzaktan ve hızlı şekilde yönetmek gerekir. Linux ve komut satırı sayesinde dosyaları yönetebilir, 
uygulamaları kurabilir, servisleri başlatabilir, hataları inceleyebilir ve sistemin kaynak 
kullanımını kontrol edebiliriz.

Bu projede nerede kullanılır?

Bu projede AWS üzerinde açacağımız EC2 sanal makinesinde Linux çalışacaktır. Sunucuya bağlandıktan 
sonra komut satırını kullanarak proje dosyalarını taşıyacak, gerekli yazılımları kuracak, PostgreSQL 
veritabanını oluşturacak, uygulamayı çalıştıracak ve logları kontrol edeceğiz. Daha sonra Kubernetes 
kurulumu ve yönetimi de Linux komut satırı üzerinden yapılacaktır.

# 4. SSH
Nedir?

SSH, kendi bilgisayarımızdan internette bulunan başka bir bilgisayara güvenli şekilde bağlanmamızı 
sağlayan bir bağlantı yöntemidir. SSH ile AWS’deki EC2 sunucusuna bağlanır ve sanki o bilgisayarın 
başındaymışız gibi komut yazabiliriz. Bağlantı sırasında bilgiler şifrelenir; yani araya giren biri 
yazdıklarımızı kolayca okuyamaz.

Hangi problemi çözer?

EC2 sunucusu fiziksel olarak bizim yanımızda değildir; Amazon’un veri merkezindedir. Bu nedenle 
sunucuya gidip ekranından işlem yapamayız. SSH, uzaktaki sunucuyu internet üzerinden güvenli biçimde 
yönetmemizi sağlar. Dosya işlemleri, program kurulumu, uygulamayı çalıştırma ve hata kontrolü gibi 
işlemleri uzaktan yapabiliriz.

Bu projede nerede kullanılır?

Bu projede AWS üzerinde oluşturacağımız EC2 Linux sunucusuna SSH ile bağlanacağız. Bağlandıktan sonra 
uygulama dosyalarını sunucuya aktaracak, gerekli yazılımları kuracak, PostgreSQL’i yapılandıracak, 
InsightDesk uygulamasını çalıştıracak ve logları inceleyeceğiz.

# 5. IP, DNS ve Port
Nedir?

IP adresi, internete bağlı bir cihazın sayısal adresidir. Örneğin AWS üzerinde oluşturduğumuz EC2 
sunucusunun internette bir IP adresi olur. DNS ise insanların sayı ezberlemek yerine alan adı 
kullanmasını sağlar; örneğin bir IP adresi yerine insightdesk.com gibi bir isim yazabiliriz. Port ise 
aynı bilgisayarda çalışan farklı uygulamaları ayıran kapı numarası gibidir. Örneğin web uygulaması 
8080 portunda, PostgreSQL veritabanı ise 5432 portunda çalışabilir.

Hangi problemi çözer?

IP adresi, internette doğru sunucunun bulunmasını sağlar. DNS, karmaşık IP adreslerini hatırlamak 
yerine kolay isimler kullanmamızı sağlar. Port ise aynı sunucuda birden fazla hizmet çalışırken gelen 
isteğin doğru uygulamaya yönlendirilmesini sağlar.

Bu projede nerede kullanılır?

Bu projede EC2 sunucusunun public IP adresi üzerinden uygulamaya erişeceğiz. İlk hafta uygulama 
örneğin 8080 portunda çalışabilir ve tarayıcıdan http://EC2_IP_ADRESI:8080 şeklinde açılabilir. Daha 
sonra istersek bir alan adı bağlayarak DNS üzerinden daha kolay erişim sağlayabiliriz. PostgreSQL 
gibi iç servisler ise kendi portlarında çalışacak, ancak güvenlik nedeniyle bu portlar doğrudan 
internete açık olmayacaktır.

# 6. Güvenlik grubu (firewall)
Nedir?

Güvenlik grubu, AWS’de bir EC2 sunucusuna hangi internet bağlantılarının girebileceğini ve sunucunun 
hangi bağlantıları dışarıya yapabileceğini belirleyen bir güvenlik duvarıdır. Sunucunun kapısındaki 
güvenlik görevlisi gibi düşünülebilir. Hangi portun açık olacağına ve kimlerin o porta 
erişebileceğine karar verir.

Hangi problemi çözer?

Bir sunucuda çalışan her hizmetin herkese açık olması güvenlik riski oluşturur. Güvenlik grubu 
sayesinde sadece gerekli bağlantılara izin veririz. Örneğin bizim bilgisayarımızın EC2’ye SSH ile 
bağlanmasına izin verebiliriz, ancak başka kişilerin SSH ile bağlanmasını engelleyebiliriz. Ayrıca 
veritabanı portunun internetten erişilmesini kapatarak verileri koruruz.

Bu projede nerede kullanılır?

Bu projede EC2 sunucusuna sadece kendi IP adresimizden SSH bağlantısına izin vereceğiz. 
Kullanıcıların web uygulamasına erişebilmesi için gerekli web portunu açacağız. Ancak PostgreSQL 
veritabanının portu olan 5432 gibi hassas portları internete açmayacağız. Böylece kullanıcılar web 
sitesine ulaşabilecek, fakat veritabanına doğrudan erişemeyecektir.

# 7. Konteyner (Container)
Nedir?

Konteyner, bir uygulamanın çalışması için gereken kodu, kütüphaneleri, ayarları ve çalışma ortamını 
tek bir paket içinde bir araya getiren yapıdır. Bu paket, uygulamanın farklı bilgisayarlarda aynı 
şekilde çalışmasını sağlar. Docker, konteyner oluşturmak ve çalıştırmak için sık kullanılan 
araçlardan biridir.

Hangi problemi çözer?

Bir uygulama kendi bilgisayarımızda çalışırken başka bir bilgisayarda çalışmayabilir. Bunun nedeni 
farklı işletim sistemi ayarları, eksik kütüphaneler veya farklı program sürümleri olabilir. 
Konteyner, uygulamanın ihtiyaç duyduğu ortamı da yanında taşıdığı için “benim bilgisayarımda 
çalışıyordu” sorununu azaltır.

Bu projede nerede kullanılır?

Bu projede web arayüzü, API servisi ve AI analiz servisi ayrı konteynerler içinde çalışacaktır. 
PostgreSQL ve Redis de konteyner olarak başlatılabilir. Böylece tüm sistemi kendi bilgisayarımızda 
veya AWS üzerindeki sunucuda aynı şekilde çalıştırabiliriz. Daha sonra Kubernetes, bu konteynerleri 
yönetmek, çoğaltmak ve gerektiğinde yeniden başlatmak için kullanılacaktır.

# 8. İmaj (Image)

Nedir?

İmaj, bir uygulamanın konteyner içinde çalışması için hazırlanmış kalıptır. Uygulamanın kodu, ihtiyaç 
duyduğu kütüphaneler, temel ayarlar ve çalıştırma talimatları bu kalıbın içinde bulunur. Konteyner 
ise bu imajdan oluşturulan çalışan haldir.

Bunu şöyle düşünebiliriz:

* İmaj = kek tarifi
* Konteyner = o tarifle yapılmış gerçek kek

Bir imajdan birden fazla konteyner oluşturulabilir.

Hangi problemi çözer?

İmaj sayesinde uygulamanın hangi dosyalarla, hangi kütüphanelerle ve hangi ayarlarla çalışacağı 
standart hale gelir. Böylece uygulamayı farklı bilgisayarlarda yeniden kurmaya çalışmak yerine aynı 
imajı kullanırız. Bu da “benim bilgisayarımda çalışıyordu” sorununu azaltır.

Bu projede nerede kullanılır?

Bu projede web arayüzü, API servisi ve AI analiz servisi için ayrı Docker imajları oluşturacağız. Bu 
imajları kendi bilgisayarımızda çalıştıracağız, daha sonra bir registry’ye göndereceğiz. Kubernetes 
de bu imajları indirerek web, API ve analiz servislerinin konteynerlerini çalıştıracaktır.

# 9. Kayıt Defteri (Registry)

 Nedir?

Kayıt defteri, oluşturduğumuz Docker imajlarını sakladığımız çevrim içi depodur. GitHub’ın kod dosyalarını saklaması gibi, registry de uygulama imajlarını saklar. Docker Hub, Amazon ECR ve GitHub Container Registry buna örnektir.

 Hangi problemi çözer?

Bir Docker imajı sadece kendi bilgisayarımızda kalırsa başka bir sunucu veya Kubernetes kümesi bu imajı kullanamaz. Registry sayesinde imajı çevrim içi bir yere yükleriz. Daha sonra başka bilgisayarlar veya sunucular aynı imajı indirip çalıştırabilir.

 Bu projede nerede kullanılır?

Bu projede web arayüzü, API ve AI analiz servisi için oluşturduğumuz Docker imajlarını bir registry’ye yükleyeceğiz. Daha sonra AWS üzerindeki Kubernetes kümesi bu registry’den imajları indirerek konteynerleri çalıştıracaktır. Registry olarak Docker Hub veya Amazon ECR kullanılabilir.

## 10. Kubernetes ve Orkestrasyon

 Nedir?

Kubernetes, konteynerler içinde çalışan uygulamaları yönetmek için kullanılan bir sistemdir. Birden fazla konteyneri başlatabilir, durdurabilir, yeniden oluşturabilir ve gerektiğinde sayılarını artırabilir. Orkestrasyon ise bu çok sayıdaki konteynerin düzenli ve otomatik şekilde yönetilmesi anlamına gelir.

 Hangi problemi çözer?

Bir uygulamanın sadece bir konteyneri varsa onu manuel olarak çalıştırmak kolaydır. Ancak web arayüzü, API, analiz servisi, veritabanı ve Redis gibi birçok konteyner olduğunda işleri tek tek yönetmek zorlaşır. Bir konteyner kapanırsa yeniden açılması, çok kullanıcı geldiğinde yeni kopyaların oluşturulması ve trafiğin doğru kopyalara dağıtılması gerekir. Kubernetes bu işleri otomatik hale getirir.

 Bu projede nerede kullanılır?

Bu projede web arayüzü, API servisi ve AI analiz servisi konteynerler içinde çalışacaktır. Üçüncü haftada AWS üzerindeki EC2 sunucusuna Kubernetes kuracağız. Kubernetes bu konteynerleri çalıştıracak, gerekli sayıda kopya oluşturacak, kapanan pod'ları yeniden başlatacak ve servislerin birbirleriyle iletişim kurmasını sağlayacaktır.

# 11. Küme (Cluster), Düğüm (Node) ve Pod

 Nedir?

Küme, Kubernetes tarafından birlikte yönetilen bilgisayarların genel adıdır. Düğüm ise bu kümenin içindeki her bir bilgisayardır. Pod ise Kubernetes içinde çalışan en küçük uygulama birimidir; genellikle bir veya birkaç konteyner içerir.

Bunu bir şirket gibi düşünebiliriz:

* Küme = bütün şirket
* Düğüm = şirketteki her bilgisayar veya çalışan alanı
* Pod = belirli bir işi yapan küçük ekip

Örneğin bir API uygulaması bir pod içinde çalışabilir. Aynı API’den üç kopya açılırsa Kubernetes üç ayrı pod oluşturur.

 Hangi problemi çözer?

Birden fazla konteyneri ve sunucuyu tek tek yönetmek zor olabilir. Küme yapısı sayesinde Kubernetes tüm kaynakları birlikte yönetir. Pod’lar sayesinde uygulamalar küçük ve düzenli birimler halinde çalışır. Bir pod kapanırsa Kubernetes yeni bir pod oluşturarak uygulamanın çalışmaya devam etmesini sağlar.

 Bu projede nerede kullanılır?

Bu projede üçüncü haftada EC2 üzerinde bir Kubernetes kümesi kuracağız. İlk aşamada bu kümede tek bir EC2 sunucusu, yani tek bir düğüm olabilir. Web arayüzü, API ve AI analiz servisi ayrı pod’larda çalışacaktır. Kullanıcı sayısı veya analiz işleri arttığında Kubernetes aynı servisten daha fazla pod oluşturarak sistemi ölçekleyebilecektir.

# 12. API (REST)

Nedir?

API, farklı uygulamaların veya uygulama parçalarının birbiriyle konuşmasını sağlayan arayüzdür. REST API ise bu iletişimin internet üzerinden belirli adresler ve HTTP istekleri kullanılarak yapılmasıdır.

Örneğin web arayüzündeki kullanıcı “Gönder” butonuna bastığında, web sayfası API’ye bir istek gönderir. API bu isteği alır, kontrol eder ve veritabanına kaydeder.

REST API’de sık kullanılan işlemler şunlardır:

* `GET`: Bilgi almak için kullanılır.
* `POST`: Yeni bilgi göndermek veya oluşturmak için kullanılır.
* `PUT` veya `PATCH`: Var olan bilgiyi güncellemek için kullanılır.
* `DELETE`: Bir bilgiyi silmek için kullanılır.

 Hangi problemi çözer?

Web arayüzü, veritabanı ve diğer servislerin doğrudan birbirine bağlı ve karışık şekilde çalışmasını önler. API, 
her bileşenin belirli kurallarla iletişim kurmasını sağlar. Böylece web arayüzü yalnızca kullanıcıdan bilgiyi 
alır; veritabanına kayıt yapma, doğrulama ve işleme gibi işleri API yürütür.

 Bu projede nerede kullanılır?

Bu projede kullanıcı web formundan geri bildirim gönderdiğinde web arayüzü API servisine `POST /api/feedback` 
isteği gönderecektir. API, başlık ve açıklamanın boş olup olmadığını kontrol edecek, geri bildirimi PostgreSQL 
veritabanına kaydedecek ve daha sonra analiz edilmesi için Redis kuyruğuna bir iş bırakacaktır.

Destek paneli de geri bildirimleri görmek için API’ye `GET /api/feedback` isteği gönderecektir. API, verileri 
veritabanından veya önbellekten alarak web arayüzüne geri gönderecektir.

# 14. Veritabanı (Database)

Nedir?

Veritabanı, uygulamadaki bilgilerin düzenli ve kalıcı şekilde saklandığı sistemdir. Uygulama kapansa veya sunucu yeniden başlasa bile veriler veritabanında durmaya devam eder. PostgreSQL, bu projede kullanacağımız veritabanı sistemidir.

Bir tabloyu Excel tablosu gibi düşünebiliriz. Ancak veritabanı çok daha güvenli, hızlı ve aynı anda birçok kullanıcı tarafından kullanılabilecek şekilde tasarlanmıştır.

Hangi problemi çözer?

Kullanıcının gönderdiği geri bildirimleri sadece uygulamanın geçici belleğinde tutarsak uygulama kapanınca bütün bilgiler kaybolur. Veritabanı, bu bilgilerin kalıcı olarak saklanmasını sağlar. Ayrıca verileri düzenli biçimde aramamıza, filtrelememize, güncellememize ve birbirleriyle ilişkilendirmemize yardımcı olur.

Bu projede nerede kullanılır?

Bu projede PostgreSQL veritabanı kullanılacaktır. Kullanıcının gönderdiği başlık, açıklama, e-posta bilgisi ve geri bildirimin durumu burada saklanacaktır. AI analiz servisi de Bedrock’tan aldığı duygu, kategori, aciliyet, özet ve önerilen yanıt bilgilerini veritabanına yazacaktır. Destek paneli, geri bildirimleri göstermek için bu verileri API üzerinden veritabanından okuyacaktır.

# 15. Önbellek (Cache)

Nedir?

Önbellek, sık kullanılan bilgileri ana kaynaktan tekrar tekrar okumak yerine, daha hızlı erişilebilen geçici bir yerde tutma yöntemidir. Bu projede Redis, önbellek olarak kullanılacaktır.

Örneğin destek paneli açıldığında geri bildirim listesini her seferinde PostgreSQL’den okumak yerine, kısa süreliğine Redis içinde tutabiliriz. Böylece aynı bilgiye daha hızlı ulaşırız.

Hangi problemi çözer?

Veritabanına sürekli aynı sorguları göndermek sistemi yavaşlatabilir. Özellikle birçok kullanıcı aynı anda paneli açarsa veritabanı gereksiz yere yoğunlaşır. Önbellek, sık istenen bilgileri hızlı şekilde sunarak yanıt süresini azaltır ve veritabanının yükünü hafifletir.

Bu projede nerede kullanılır?

Bu projede destek panelindeki geri bildirim listesi Redis önbelleğinden sunulacaktır. Kullanıcılar paneli açtığında API önce Redis’te hazır veri olup olmadığına bakacaktır. Veri varsa doğrudan Redis’ten dönecektir. Yeni bir geri bildirim eklendiğinde veya analiz sonucu değiştiğinde ise önbellek güncellenecek ya da silinerek eski bilgi gösterilmesi engellenecektir.






