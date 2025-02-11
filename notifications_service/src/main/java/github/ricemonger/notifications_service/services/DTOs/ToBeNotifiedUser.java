package github.ricemonger.notifications_service.services.DTOs;

public record ToBeNotifiedUser(String chatId,
                               Boolean privateNotificationsEnabled,
                               Boolean publicNotificationsEnabled,
                               Boolean ubiStatsUpdateNotificationsEnabled,
                               Boolean tradeManagerNotificationsEnabled,
                               Boolean authorizationNotificationsEnabled) {
}
