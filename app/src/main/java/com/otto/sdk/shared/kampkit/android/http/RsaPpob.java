package com.otto.sdk.shared.kampkit.android.http;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class RsaPpob {
  public String getSignature (long dateTimestamp) {
    String PRIVATE_KEY = getPrivateKey();
    String PUBLIC_KEY = getPublicKey();

    String requestBody = "{\"grant_type\":\"client_credentials\",\"scope\":\"PPOB-client\"}";
    // Instant timestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    // ZonedDateTime dateTimestamp = timestamp.atZone(ZoneId.of("Asia/Jakarta"));
//    OffsetDateTime timestamp = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    // String dateTimestamp = DateTimeFormatter.ISO_DATE_TIME.format(timestamp);
//    long dateTimestamp = Instant.now().getEpochSecond();
    String stringToSign = requestBody + "|" + dateTimestamp;
    String stringToVerify = requestBody + "|" + dateTimestamp;
//
//    System.out.println("stringToSign: " + stringToSign);
//    System.out.println("========================================");
//    System.out.println("stringToVerify: " + stringToVerify);
//    System.out.println("========================================");
//    System.out.println("PRIVATE_KEY: " + PRIVATE_KEY);
//    System.out.println("========================================");
//    System.out.println("PUBLIC_KEY: " + PUBLIC_KEY);
//    System.out.println("========================================");
    String signature = null;
    try {
       signature = signSHA256RSA(stringToSign, PRIVATE_KEY);
//      System.out.println("signature: " + signature);
//      System.out.println("========================================");
      boolean isVerified = verifySHA256RSA(stringToVerify, signature, PUBLIC_KEY);
//      System.out.println("isVerified: " + isVerified);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return signature;
    // String decrypted = decrypt(encrypted, SECRET_KEY);
    // System.out.println("Decrypted Hex : " + decrypted);

  }

  private static String getPrivateKey(){
    // Not a real private key! Replace with your private key!
    // String privateKey = "-----BEGIN PRIVATE KEY-----\n"
    //         + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDb8atZHW7/9ww/\n"
    //         + "YS0nfVm6W7mDBiMYNRh76r7XCuj8inRiQbDJXX96U1LP+QyDNoCA5vwNV+FOZfcg\n"
    //         + "l0JwEUwk+fYBG4P8MIcLV+Ea3gA2y/oKOvKFqmeFvX33UT3JlvS0IwWI+5hDHJvK\n"
    //         + "7XDbXo/nU3wbBUuP11aVYVGmCAY80YoEp1V6/HZAQs2a3Dl3XKfazlMCKnodaeFR\n"
    //         + "8q29mz0pWTjFFtcGvjIRH4ZI8boJSFV9hy1mshb1kLG1z35bF0TqM33+OyqXOyQW\n"
    //         + "uXJl/ZnW/Y+vZ54gkylmr7yBL7oo/Myt8EFTRhlsF5Ge9AdzHAtWO7t8yqiVNdX+\n"
    //         + "f3gWe5ObAgMBAAECggEBAL9Dbm1FckR28pifCeRAVmn/39f320buzUbdQW3BmBAc\n"
    //         + "2s6jL3g8rWGT2fzvJ5lnKOJve5qi5FQHyZr2zpikAiJZmDsoUWNaU+ViW2fAzFDa\n"
    //         + "mqqQzlu8bt/XhBYw+Nk4Q73ugp63o5Yg573VM4IgvmU8rTzWIvqIGUw34AfRDJOP\n"
    //         + "SrmQkZQOjKhl+IB0fsldz1o//yeww/bhJ8d5zaHnxF1AxiYVJvP1EpGkayLCeSAz\n"
    //         + "b/CTYm7Fn1pVPakYOb308me5dJxOqiprnyoiV7P8wlDFWsyC9uoz7TLihfkQ/mo4\n"
    //         + "/4K6IPqYBl53WX9NS/dycXy5CVovnhH/a8d0c6lQWlkCgYEA/P/WpcXBCmDvzRHX\n"
    //         + "Y0LcG9pZ5H7/UTsMqqFoPAEozf7tIvmMeMr0AfCUD0IeNvyuRVy3LFiduSU6rpny\n"
    //         + "3I2qb7z5FiSrNiF/AqYIE5kjoHnXChRd9c8HZQNpBM4O1FSmpkXAvzPlbXpexNjV\n"
    //         + "nsw+qwra3GhzPSdkordDG1ERuj0CgYEA3o13s0/pUavO0FW3a+5Tj7diBZvsjFLn\n"
    //         + "kkJoy3EAzO0DJtRDMpt8ZO5esd7ISFdQTN47R9DXOeRZcarZhjwO9ox69TG4SAp+\n"
    //         + "zdeD+JWt7eTBKvIIvT3BVOmIUq2KamvlqgR5pQDzAEz0Up4C3hXvXwN0pQLdDbqY\n"
    //         + "fZfrPxgOWrcCgYBO3T0+zYcgQhleNlqKwZO1RvpQhNn3q02GfuvsEHx0wiuPhHSq\n"
    //         + "TrGDTUgJ/ZVHlLqKOp2KwFDiHt3NAYY4BB0F2cVXhrSsGgLqg3DvEwSNCI3kwXaM\n"
    //         + "oi1u6oI5EjaDL6QHHqU3tOimmMej2ue0KXy7epXAsPqDLJ1tL21GvZ2mDQKBgGn3\n"
    //         + "iChMA3dMzCOsvLJaf1Ut5zFuhdpm2zZsAoPj4ZKxw89oQDrHiBpy2Ynt3wV69ZQ0\n"
    //         + "OJvAV8qL7P7Pb9WXoTRJvX4kV2+N98TBOmhRtMhSJJvzNLAOyJBKxgs/8GktyOQE\n"
    //         + "T3t+kTXybl8sN4dRgHGILrEnROP1lggvHuci6CrPAoGAGwXU2MGNsLzjxLcXY/Uk\n"
    //         + "1ydqXfeBQDzCI7v/DVZV2Bs70qHm8pdFgteImcwDlvgzbirzuGlzSqdIYmsJdM4f\n"
    //         + "xSttmXpWc8RMDtxRQUhO8fcSOHuRDPkq30kdC84qSXB7rvm9h9EMrt2SjRXiyz09\n"
    //         + "Ok3Ur7BTHanjnnAsq6NaPVo=\n"
    //         + "-----END PRIVATE KEY-----";
    // String privateKey = "-----BEGIN PRIVATE KEY-----\n"
    //     + "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC60aYdsC0zG27s\n"
    //     + "oXyeXrhxnxezvRmtumh+FittVVBXmAd7Gag6uOyuHOmRJOyGxy99+NIc1gXdUWtC\n"
    //     + "fjhoaHZ51pKM53SL8C1sjpstZIJax3K0YkIYbk9lcjczQVJmC6sTwVZd11Ahy7aY\n"
    //     + "t1Tp4kDZUx21oOVN3WwQigvpjyEXVJ4gS2+FTkX41twmMMgIHD4R47YWRbqx1VQj\n"
    //     + "bCTBveheaEeE4+FzrMDnxut2ZQ/pmRwQ0/jFszWWSs4DTKj4m0Zqt5KZYSZYcOKa\n"
    //     + "UOAQUy6m1EpuuQMXaCpRtNIPalDasVC/vnaWDfrekz+O0DG6beUKvzIV7r16v1RG\n"
    //     + "fNm8ow+vAgMBAAECggEBAKLnxe8FfaSfO27ksoJ3HmcVz9lQAGBZuFyagGvqhRCc\n"
    //     + "5YkWqnfqYZjCZXnHtOkiL+utWasDhkGjieCFqDKDRSyhS1HLUvnkCfgrsm02w+Td\n"
    //     + "HszlEbW0G0/e9Pug7nVFn3yujt1tr9aIiUVKOKVqQhpAPBvxnYL0nhMeDZ0wzi5E\n"
    //     + "gWOGXDSR8yo1EY2Ygo29+dizvEDEQ40Xotj6OA6nsACiP0Ch78dkB1RVTfr0Isye\n"
    //     + "XEqa/YcQUs7JS5Lvjip3i5gLx8/JR1fppFwkXnJf9APHsSNnERaNdki3UK9L/H7j\n"
    //     + "FK4tDDwgd3FW1nhpRgf+41aud8sD8KLckn/kxMd9CkECgYEA5mNEIdzSH2V1bMGV\n"
    //     + "wblEP1IAI3SWZ8tHSzYDHKpr133LNg6TG6s1CjFk+8z6XjfuWIrZs6rc7fuKi7FR\n"
    //     + "v8ZogU942wct/E2WsUtLZBMnjNB2frOsbRubHFL+iPA7F9xWCNlVRQJiAfXQgxSA\n"
    //     + "qb+TiJDn3sO7p6AiY1nWYvgaDakCgYEAz5Zu9mHpprEEPh+XtYWVAAcW7C94gpNK\n"
    //     + "rg98fIDA5ay60DDE2Jdob2CxqaK0sG+0StIdtkAQKmbJh7wrG0BBr82krF/BNQn4\n"
    //     + "S1YKy2QVOTaNuTBufV/YiV8IvWHtbV/DR/dRcOUpVM32ZVN7kEnQsDOXyzA+0s8I\n"
    //     + "SLjw6W0jmZcCgYEAt0j9qRruJ4Al/RG9PFNR0jZAWfSku4pi+5jKvQFvrg4rnqPV\n"
    //     + "sMtx9Nv7BL+75z0D53j/XQpvnPzi6eN1r+X+ufEpqsZ5dSGc4wuNvN3oNwSZYI88\n"
    //     + "e8jci5oTWYoU2RS3dW7BgZc1tW2EJuQmv+TjUGBHt1dscRzafsO35SCQSZECgYEA\n"
    //     + "qFerdrzieV6WPTbrlbZVpecZ7MF2bhW6ezttzZ2wUNFp0gmqqKbod2In0ZR0CZ0c\n"
    //     + "zYxlsfVz5p7ikePD5V6Ppl0TTQJ+MV/7bkTBjj2EhJg8CFFBWQUQGh7qfIJcw9Tk\n"
    //     + "DYmADxiYEwDP703rJ5Dk3u+ICqv+VoC0jB0GqZXrR9MCgYBWXI+lVWNMEcvo6kk5\n"
    //     + "4fSNSwE/JNf8nyJ7SHTu/GmTZ76bHPs96SpefC+1bdTPjBQ/JrPObw8hND2ZjAcU\n"
    //     + "Zxpjt/7L8Q+N0wlljEl3Yb0f3Wl632sYSkBEvu3Zh16yOLENu05SSTYcOTMpkzKJ\n"
    //     + "ND1QLlrqqEW2qJOcgJELqC8QBg==\n"
    //     + "-----END PRIVATE KEY-----";
    // String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
    // "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDo1Jat7VWIAbEA\n" +
    // "xrxVs87ICgD+8eXFt4n/AvB8jKkIuQlb3idgh20z+RqdYLwwE6SPyOmuqce4E//r\n" +
    // "36aXzTvcrtjMHSEtXoJu39hX0pVXEJNrrwpinmQWuxgADW8/sBDcq/VYkvuxpJga\n" +
    // "tRk+h/PSXWJcy+SmTqlZhVDIXsWAmQzcTbToiEMvlFgiBJaA85FRRQezFPNC5IGc\n" +
    // "UPGOj6gNeXhTeq+1lY63kbqYdudYrLMWp+yADEK0c0rcDjFmjLCzm/wZxwMrZbys\n" +
    // "tlRDvGtRvw4m9kXKX64pwzh6UviSw40RlZm9guUrRCzYMmTAe53PyImLqWC5F7QL\n" +
    // "kadErsYFAgMBAAECggEBAJKnVUG4fGduqU8T3w0cdOMLahUryM8BDKrT7yaEcvRL\n" +
    // "mNmE1Mi4hNB314vJor+ln8Hxiz+5MVOBRXINLaHwZARdHbFzp7IsFXxBAsROoEJ2\n" +
    // "7mSlnKUBn4mxzyw5ShmTjQtyRNfFvUPnqOcpqvz4JVwb9hj9VMhsQ79fecK77ytQ\n" +
    // "CHkRkYEk3C364lM5Zb2T6OCo2rCmn8mBCjyAuuzD1EBCNswQrg5SRpBsxbTRmQia\n" +
    // "SfTYt9sJQzuh2rPZZcaOsyFOrK7TCTKjBiyjgebgej/BaZHrsRFvdCJuzq5aeLht\n" +
    // "DdKBtQwgLPI46WiBq+XtgLPmOcx6B816r1FLZ/BOLwECgYEA9ZLvpK8cOryWgjYj\n" +
    // "icA5Q8/Kc1fFL91hOB3XTm0FQp/hEFocC0oU8ak31qjYQWH0zHLTltZPW2szWZ1N\n" +
    // "Sc/CKCZ+s0Nib7p74RKpW3GOdF8xyRTNEjObGXNcD78qfAnNYMQZAxEhwRw+bwko\n" +
    // "E/VmreDFVExSCs6bJ5ypn9YXNlsCgYEA8rclo1uddqua6BdduETvP69ov3XlStZs\n" +
    // "8DT7FFyvssxQn3ORe1jYmHmXq141x+9BgocJo4f+mdxE9qrIrwf6mOxb6GgOf4+Y\n" +
    // "71l+l+nHwZAl0dmrXa2WG/G4naKxLKQdB8pp4zBqUAAaNXcO86xc7u7FZ7xwnIR3\n" +
    // "zj6EcwSuYx8CgYEA4h53DOO0kZz+1Uz2ciuOTxNw7b3+snEcyKgpjWwzMMK4YO8n\n" +
    // "7Xk8SbyvzrdP0PxahTqh+7y/+4R9FuIRvYpQ09mGRpggdpJ26KrfSHUKX7sbw3Sk\n" +
    // "00KzaL7hIRq8CtSXyKcoBLvsobzY/DKpNZ97+VUDPfi8fnj5QoMtaSCf5QsCgYEA\n" +
    // "oNepw5Wkxc12vDMv0jssaQGu4qIYLv3l91DEynuZm102WFRXpmv8LR722YN3FGWC\n" +
    // "/iLKymoG2KNVuDj5ALTC5rBLP7olUXcxm/usnrM3wxfpWCVtIIk2HpKTCzOTQcgH\n" +
    // "zO/gN8QPz7AhriiV6fjhJR1qCBKidbZ7Rlu7nhUOE3UCgYAGdJvilXQgH3q5iv2s\n" +
    // "GxyaW1gI11dsvrSh2REsvF/ToaOm6vewCvDFUY5uqssyhx9rNMlaTDKV2KoUYz7g\n" +
    // "2UdjDzh4xff7jJ2ZS0pabg3aukCNQrwLQqx1CtCMpnocHpYLiSrAACQc5qFD+33/\n" +
    // "DFSUXCDW7ZKxo+tVSRE9YtEGDQ==\n" +
    // "-----END PRIVATE KEY-----\n";
    String privateKey = "-----BEGIN PRIVATE KEY-----\n"
        + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDGnKaCYMgjHBF1\n"
        + "sAwUUjNmrzw3JXzKaP9d3so3jZ8M7VD03Jc3q5KnkPQ+HpHURkM2O8Px2ldFZdlZ\n"
        + "VoS9YS9RZT0AdPokmuh/7f1NNKvJFapdzxTnDLNI87L3sk1q/1oKzyi1JDg/kP0J\n"
        + "TAjJnPdd1/bv86EpCVT4B8xb0WE69X8664iXJHeJBceYQrcwIRf70LxWOWE6GddG\n"
        + "ENUEkJVQEFiNpEMZ3ZG9Rd5Hf/VV4w6LNf4FRtE6UPIUK4zC4Os523Kmo1bIiAdk\n"
        + "Cdo4QgaO3gcARwkgMQnaEuxdaFX/lzQ2RbFVuXDb5Q2kYcUgj6PGMkoU7qa5nl5E\n"
        + "q/ZAdWjRAgMBAAECggEAQmCyaZqM6OcFks6eiuAaG7RvLIMT3NYQmr/N15k2Ueyy\n"
        + "hNOAmnN2FRil+GjhFg/RgLuith7h0ejDJk97F4v2c36hT5jIzRkUoUTZxkbXLuJV\n"
        + "yPpfowSkmroP0ya9JwhmgHxRv5B2WBBN8SB/Q1p5ddyqw/EpqHxc8HQywXENzNUO\n"
        + "zrwROU16qQpiZPnkpF29tFT/ZTm4wGAbOLGud8oxCo9mxoXEfMSBF7Rku9rt8JL4\n"
        + "aGuNeY2du2oooCurc2LQO9dTF0CVe6Nl83LOQ5Ay7lQfWs9ha9+Eyo5YALn5v+BE\n"
        + "hKxzwZj9tvVHkr963Vrv/VbKiVNpgF5VOyqwSouhDQKBgQDkxi8GMuGiKgXhBkXJ\n"
        + "v8TGdXBurFXgGAZImxHG7LqHPRQfowBb1N5t0sVhjgAMpHaA2w94M/Hu8viP/AD9\n"
        + "T08hy/oOlZ9lMeFXU9PxdSiOl+mU9SOBJy2RoVmeqVOYBvjDu6P6qhcztQu9zWAM\n"
        + "eiX9tO4W7zhfx8pwfi+NFoHEewKBgQDeP4vUabkImtFXum+7AKqmYNCUm0cG8ls7\n"
        + "cNal6dMrh9Lvz0XE2qYIjV33vJ/OwHGsSE3Rk9EubMtnPrKioXD+uuWrYRfN9g+R\n"
        + "a2EHfDr410ewvPkl64PFhkKAT6upjdyIVa/E4eFWktJc8RHy2JRXLr7vlAXiqkDL\n"
        + "aDZcIQ7kIwKBgQDjqua5QIGTJEbdfaSN9i4ZYl4VhaOgc8gtEUVLteKbG4mfQlPl\n"
        + "D56bpJmRUAN0kWbtLRPkB63IojwC7kL906NoetnLEdOinVUn1uCg9nr6DOwDtUE0\n"
        + "Eo0i1HOfldr+/FEsWNwDZKnaZ13b8TqYsCaGqTwXztFHYEhyUuF3guJ5aQKBgGzn\n"
        + "kqtpiwQOaRql/u4Rq42Pz2WimTFsPQxphKUUaSfhjJl6ZePKiKWUMEGfKmfzoUAc\n"
        + "xhU2a8BmC13yOTK8DVSlXLmYAEQqmQJXLdqvI26gfz65uxS7zXxTB+88GxAgtboQ\n"
        + "0uqVDQiRNYfZkUQAoN//xgw76o5hgxKpZEnM4TElAoGAdzvQffsdKywd7Y7xuLR9\n"
        + "kOQweXY+MdBjad2Sf2ADUoPLCnF/V9UGzOgh6Er631sZ6imMB976iiDruvrtHjHV\n"
        + "kDkNYjreall+DluF1DrmMlIW5Bts8Xk86KuJ/LihX2jJRDhGWifkfKocLRSSKGgo\n"
        + "UBiXH5IU6kwZM1z/bTJG0Po=\n"
        + "-----END PRIVATE KEY-----";


    // Remove markers and new line characters in private key
    return privateKey.replaceAll("-----END PRIVATE KEY-----", "")
        .replaceAll("-----BEGIN PRIVATE KEY-----", "")
        .replaceAll("\n", "");
  }

  private static String getPublicKey(){
    // String publicKey = "-----BEGIN PUBLIC KEY-----\n"
    //     + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAutGmHbAtMxtu7KF8nl64\n"
    //     + "cZ8Xs70ZrbpofhYrbVVQV5gHexmoOrjsrhzpkSTshscvffjSHNYF3VFrQn44aGh2\n"
    //     + "edaSjOd0i/AtbI6bLWSCWsdytGJCGG5PZXI3M0FSZgurE8FWXddQIcu2mLdU6eJA\n"
    //     + "2VMdtaDlTd1sEIoL6Y8hF1SeIEtvhU5F+NbcJjDICBw+EeO2FkW6sdVUI2wkwb3o\n"
    //     + "XmhHhOPhc6zA58brdmUP6ZkcENP4xbM1lkrOA0yo+JtGareSmWEmWHDimlDgEFMu\n"
    //     + "ptRKbrkDF2gqUbTSD2pQ2rFQv752lg363pM/jtAxum3lCr8yFe69er9URnzZvKMP\n"
    //     + "rwIDAQAB\n"
    //     + "-----END PUBLIC KEY-----";
    String publicKey = "-----BEGIN PUBLIC KEY-----\n"
        + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxpymgmDIIxwRdbAMFFIz\n"
        + "Zq88NyV8ymj/Xd7KN42fDO1Q9NyXN6uSp5D0Ph6R1EZDNjvD8dpXRWXZWVaEvWEv\n"
        + "UWU9AHT6JJrof+39TTSryRWqXc8U5wyzSPOy97JNav9aCs8otSQ4P5D9CUwIyZz3\n"
        + "Xdf27/OhKQlU+AfMW9FhOvV/OuuIlyR3iQXHmEK3MCEX+9C8VjlhOhnXRhDVBJCV\n"
        + "UBBYjaRDGd2RvUXeR3/1VeMOizX+BUbROlDyFCuMwuDrOdtypqNWyIgHZAnaOEIG\n"
        + "jt4HAEcJIDEJ2hLsXWhV/5c0NkWxVblw2+UNpGHFII+jxjJKFO6muZ5eRKv2QHVo\n"
        + "0QIDAQAB\n"
        + "-----END PUBLIC KEY-----";


    // Remove markers and new line characters in public key
    return publicKey.replaceAll("-----END PUBLIC KEY-----", "")
        .replaceAll("-----BEGIN PUBLIC KEY-----", "")
        .replaceAll("\n", "");
  }

  // Create a hex encoded signature using SHA256/RSA.
  private static String signSHA256RSA(String toSign, String privateKey) throws Exception {
    byte[] privateKeyBytes = android.util.Base64.decode(privateKey,android.util.Base64.DEFAULT);

    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");

    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(kf.generatePrivate(spec));
    privateSignature.update(toSign.getBytes("UTF-8"));
    byte[] signatureBytes = privateSignature.sign();
    // return hexEncode(signatureBytes);
    return byteArrayToHexString(signatureBytes);
  }

  private static boolean verifySHA256RSA(String toVerify, String signature, String publicKey) throws Exception {
    byte[] publicKeyBytes = android.util.Base64.decode(publicKey,android.util.Base64.DEFAULT);

    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");

    Signature publicSignature = Signature.getInstance("SHA256withRSA");
    publicSignature.initVerify(kf.generatePublic(publicKeySpec));
    publicSignature.update(toVerify.getBytes("UTF-8"));

    byte[] signatureBytes = hexStringToByteArray(signature);
    boolean isVerified = publicSignature.verify(signatureBytes);
    return isVerified;
  }

  // https://gist.github.com/easternHong/6ca75a7fdce15c6a23d3
  public static String byteArrayToHexString(byte[] bytes) {
    // final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    // 'A', 'B', 'C', 'D', 'E', 'F' };
    final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
    int v;
    for (int j = 0; j < bytes.length; j++) {
      v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
      hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
      hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
    }
    return new String(hexChars);
  }

  public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    if (len % 2 == 1) {
      throw new IllegalArgumentException("Hex string must have even number of characters");
    }
    byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
    for (int i = 0; i < len; i += 2) {
      // Convert each character into a integer (base-16), then bit-shift into place
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }
}

