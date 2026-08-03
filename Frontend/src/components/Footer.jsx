import React, { useState } from 'react';
import {
    Star,
    Heart,
    ShieldCheck,
    Award,
    Lock,
    Send,
    CheckCircle2,
    Mail,
    Store,
    HelpCircle,
    FileText
} from 'lucide-react';
import './Footer.css';

export default function Footer() {
    const [email, setEmail] = useState('');
    const [subscribed, setSubscribed] = useState(false);

    const handleSubscribe = (e) => {
        e.preventDefault();
        if (email.trim()) {
            setSubscribed(true);
            setEmail('');
            setTimeout(() => setSubscribed(false), 4000);
        }
    };

    return (
        <footer className="footer">
            <div className="max-container footer-container">
                {/* Main Footer Top Grid */}
                <div className="footer-top">
                    {/* Brand & Newsletter Column */}
                    <div className="footer-brand-col">
                        <div className="footer-logo">
                            <div className="footer-logo-icon-box">
                                <Star className="footer-logo-star" />
                            </div>
                            <span className="footer-logo-text">
                                Rate<span className="logo-nest">Nest</span>
                            </span>
                            <span className="footer-logo-version">v1.0</span>
                        </div>

                        <p className="footer-brand-desc">
                            Empowering consumers with authentic store ratings and helping local businesses build lasting customer trust.
                        </p>

                        {/* Newsletter Subscription */}
                        <div className="footer-newsletter">
                            <h4 className="footer-newsletter-title">Stay updated with RateNest</h4>
                            {subscribed ? (
                                <div className="footer-subscribed-msg">
                                    <CheckCircle2 className="footer-success-icon" size={16} />
                                    <span>Thank you for subscribing!</span>
                                </div>
                            ) : (
                                <form onSubmit={handleSubscribe} className="footer-newsletter-form">
                                    <input
                                        type="email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        placeholder="Enter your email"
                                        required
                                        className="footer-email-input"
                                    />
                                    <button type="submit" className="footer-subscribe-btn" aria-label="Subscribe">
                                        <Send size={15} />
                                    </button>
                                </form>
                            )}
                        </div>

                        {/* Social Links */}
                        <div className="footer-socials">
                            <a href="https://twitter.com" target="_blank" rel="noreferrer" className="footer-social-link" aria-label="Twitter">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z" />
                                </svg>
                            </a>
                            <a href="https://github.com" target="_blank" rel="noreferrer" className="footer-social-link" aria-label="GitHub">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                                    <path fillRule="evenodd" clipRule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.53 1.032 1.53 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" />
                                </svg>
                            </a>
                            <a href="https://linkedin.com" target="_blank" rel="noreferrer" className="footer-social-link" aria-label="LinkedIn">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M19 3a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h14m-.5 15.5v-5.3a3.26 3.26 0 0 0-3.26-3.26c-.85 0-1.84.52-2.28 1.3v-1.11h-2.79v8.37h2.79v-4.93c0-.77.62-1.4 1.39-1.4a1.4 1.4 0 0 1 1.4 1.4v4.93h2.75M6.88 8.56a1.68 1.68 0 0 0 1.68-1.68c0-.93-.75-1.69-1.68-1.69a1.69 1.69 0 0 0-1.69 1.69c0 .93.76 1.68 1.69 1.68m1.39 9.94v-8.37H5.5v8.37h2.77z" />
                                </svg>
                            </a>
                            <a href="mailto:support@ratenest.com" className="footer-social-link" aria-label="Email Support">
                                <Mail size={16} />
                            </a>
                        </div>
                    </div>

                    {/* Navigation Links Grid */}
                    <div className="footer-links-grid">
                        {/* Column 1: Discover */}
                        <div className="footer-link-col">
                            <h4 className="footer-col-title">
                                <Star size={15} className="footer-col-title-icon" />
                                Discover
                            </h4>
                            <ul className="footer-links-list">
                                <li><a href="#curated-picks">Top Rated Stores</a></li>
                                <li><a href="#categories">Browse Categories</a></li>
                                <li><a href="#recent-reviews">Recent Ratings</a></li>
                                <li><a href="#trending">Trending Places</a></li>
                            </ul>
                        </div>

                        {/* Column 2: For Businesses */}
                        <div className="footer-link-col">
                            <h4 className="footer-col-title">
                                <Store size={15} className="footer-col-title-icon" />
                                For Businesses
                            </h4>
                            <ul className="footer-links-list">
                                <li><a href="#business-cta">Claim Your Store</a></li>
                                <li><a href="#owner-dashboard">Owner Dashboard</a></li>
                                <li><a href="#verification">Store Verification</a></li>
                                <li><a href="#analytics">Rating Insights</a></li>
                            </ul>
                        </div>

                        {/* Column 3: Support */}
                        <div className="footer-link-col">
                            <h4 className="footer-col-title">
                                <HelpCircle size={15} className="footer-col-title-icon" />
                                Support
                            </h4>
                            <ul className="footer-links-list">
                                <li><a href="#how-it-works">How It Works</a></li>
                                <li><a href="#faqs">Help Center & FAQs</a></li>
                                <li><a href="#guidelines">Community Guidelines</a></li>
                                <li><a href="#contact">Contact Support</a></li>
                            </ul>
                        </div>

                        {/* Column 4: Legal */}
                        <div className="footer-link-col">
                            <h4 className="footer-col-title">
                                <FileText size={15} className="footer-col-title-icon" />
                                Legal & Safety
                            </h4>
                            <ul className="footer-links-list">
                                <li><a href="#privacy">Privacy Policy</a></li>
                                <li><a href="#terms">Terms of Service</a></li>
                                <li><a href="#cookies">Cookie Policy</a></li>
                                <li><a href="#trust-safety">Trust & Security</a></li>
                            </ul>
                        </div>
                    </div>
                </div>

                {/* Feature Widgets / Highlights */}
                <div className="footer-widgets">
                    <div className="footer-widget">
                        <div className="footer-widget-title">
                            <ShieldCheck className="footer-widget-title-icon" />
                            <span>Verified Ratings</span>
                        </div>
                        <p>100% authenticated feedback from real shoppers and users, keeping reviews transparent and trustworthy.</p>
                    </div>

                    <div className="footer-widget">
                        <div className="footer-widget-title">
                            <Award className="footer-widget-title-icon" />
                            <span>Business Empowerment</span>
                        </div>
                        <p>Store owners gain actionable rating insights and customer management tools to continuously improve.</p>
                    </div>

                    <div className="footer-widget">
                        <div className="footer-widget-title">
                            <Lock className="footer-widget-title-icon" />
                            <span>Privacy & Security</span>
                        </div>
                        <p>Your user profile and feedback history are protected with industry-standard privacy safeguards.</p>
                    </div>
                </div>

                {/* Footer Bottom Bar */}
                <div className="footer-bottom">
                    <div className="footer-copyright">
                        © {new Date().getFullYear()} RateNest, Inc. All rights reserved.
                    </div>

                    <div className="footer-credits">
                        <span>Crafted with</span>
                        <Heart className="footer-heart-icon" />
                        <span>for genuine reviews & great local stores</span>
                    </div>
                </div>
            </div>
        </footer>
    );
}

