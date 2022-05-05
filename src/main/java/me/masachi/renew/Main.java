package me.masachi.renew;

import com.azure.identity.*;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import okhttp3.Request;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
                .clientId(args[0])
                .username(args[1])
                .password(args[2])
                .tenantId(args[3])
                .build();

        final TokenCredentialAuthProvider tokenCredAuthProvider =
                new TokenCredentialAuthProvider(Collections.singletonList("https://graph.microsoft.com/.default"), usernamePasswordCredential);

        GraphServiceClient<Request> graphClient = GraphServiceClient.builder().authenticationProvider(tokenCredAuthProvider).buildClient();
        // 参数错误的时候会报错，而且也获取不了，jar包里面自己处理了(e.printStackTrace())
        // 获取用户
        User user = graphClient.me().buildRequest().get();
        // 查看用户邮件列表
        MessageCollectionPage iMessageCollectionPage = graphClient.users(user.userPrincipalName).messages().buildRequest().select("sender,subject").get();
        List<Message> messageList = iMessageCollectionPage.getCurrentPage();
        System.out.printf("运行时间：%s —— 共有%d封件邮件%n", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), messageList.size());
    }

}
