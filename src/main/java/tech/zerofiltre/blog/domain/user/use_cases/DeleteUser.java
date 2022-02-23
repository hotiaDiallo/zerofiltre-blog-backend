package tech.zerofiltre.blog.domain.user.use_cases;

import tech.zerofiltre.blog.domain.*;
import tech.zerofiltre.blog.domain.article.*;
import tech.zerofiltre.blog.domain.article.model.*;
import tech.zerofiltre.blog.domain.error.*;
import tech.zerofiltre.blog.domain.user.*;
import tech.zerofiltre.blog.domain.user.model.*;

import java.util.*;

public class DeleteUser {

    private final UserProvider userProvider;
    private final ArticleProvider articleProvider;

    public DeleteUser(UserProvider userProvider, ArticleProvider articleProvider) {
        this.userProvider = userProvider;
        this.articleProvider = articleProvider;
    }

    public void execute(User currentUser, long userIdToDelete) throws ResourceNotFoundException, ForbiddenActionException {

        User foundUser = userProvider.userOfId(userIdToDelete).orElseThrow(() ->
                new ResourceNotFoundException("We could not find the user you want to delete", String.valueOf(userIdToDelete), Domains.USER.name()));

        if (!currentUser.getRoles().contains("ROLE_ADMIN") && currentUser.getId() != foundUser.getId())
            throw new ForbiddenActionException("You can only delete your own account", Domains.USER.name());

        List<Article> userArticles = articleProvider.articlesOf(foundUser);
        if (userArticles.isEmpty()) {
            userProvider.deleteUser(foundUser);
        } else {
            foundUser.setExpired(true);
            userProvider.save(foundUser);
        }


    }
}